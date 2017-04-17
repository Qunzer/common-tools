package common.distributedLock;

import com.google.common.base.Strings;
import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;

/**
 * Created by rq on 2017/04/17.
 */
public class RedisDistributedLockImpl implements DistributedLock {
    private static final long DEFAULT_LOCK_HOLD_TIMEOUT = 60 * 60 * 24 * 30 * 12;//锁的默认持有时间，单位秒---变相去掉这个概念，将持有时间设置得很大
    private static final TimeUnit DEFAULT_LOCK_HOLD_TIME_UNIT = TimeUnit.SECONDS;
    private static final int LOCK_WAIT_TIMEOUT = 100;//获取lock重试等待时间
    private static final int DEFAULT_RETRY_COUNT = 5;//获取lock的重试次数
    private Jedis jedis;

    protected RedisDistributedLockImpl(Jedis jedis) {
        this.jedis = jedis;
    }

    @Override
    public void lock(String lockKey, long lockAcquireWaitSeconds) {
        int retryCount = (int) lockAcquireWaitSeconds / LOCK_WAIT_TIMEOUT;
        lock(lockKey, LOCK_WAIT_TIMEOUT, retryCount, DEFAULT_LOCK_HOLD_TIMEOUT, DEFAULT_LOCK_HOLD_TIME_UNIT);
    }

    @Override
    public void lock(String lockKey, long lockAcquireWaitSeconds, long lockHoldDurationSeconds) {
        int retryCount = (int) lockAcquireWaitSeconds * 1000 / LOCK_WAIT_TIMEOUT;
        lock(lockKey, LOCK_WAIT_TIMEOUT, retryCount, lockHoldDurationSeconds, DEFAULT_LOCK_HOLD_TIME_UNIT);
    }

    private void lock(String lockKey, long lockHoldDuration, TimeUnit lockHoldDurationTimeUnit) {
        lock(lockKey, LOCK_WAIT_TIMEOUT, DEFAULT_RETRY_COUNT, lockHoldDuration, lockHoldDurationTimeUnit);
    }

    private void lock(String lockKey, long lockAcquireWaitMilliseconds, int retryCount, long lockHoldDuration, TimeUnit lockHoldDurationTimeUnit) {
        int hasRetiedCount = 0;
        while (hasRetiedCount < retryCount) {
            if (tryLock(lockKey, lockHoldDuration, lockHoldDurationTimeUnit)) {
                return;
            }
            try {
                Thread.sleep(lockAcquireWaitMilliseconds);
            } catch (InterruptedException e) {
                throw new LockAcquisitionException(e);
            }
            hasRetiedCount++;
        }
        throw new LockAcquisitionTimeoutException("can not acquire the lock '" + lockKey + "' in " + (lockAcquireWaitMilliseconds * retryCount / 1000) + " seconds");
    }

    @Override
    public void lock(String lockKey) {
        lock(lockKey, DEFAULT_LOCK_HOLD_TIMEOUT, DEFAULT_LOCK_HOLD_TIME_UNIT);
    }

    @Override
    public boolean tryLock(String lockKey) {
        return tryLock(lockKey, DEFAULT_LOCK_HOLD_TIMEOUT, TimeUnit.SECONDS);
    }

    private String buildLockValue(long timeout, TimeUnit timeUnit) {
        long timeoutMilliseconds = timeUnit.toMillis(timeout);
        return String.valueOf(System.currentTimeMillis() + timeoutMilliseconds);
    }


    @Override
    public void unlock(String lockKey) {
        long now = System.currentTimeMillis();
        String lockValueString = jedis.get(lockKey);
        if (Strings.isNullOrEmpty(lockValueString)) {
            return;
        }
        long lockValue = Long.parseLong(lockValueString);
        //锁持有超时了就不用删除了
        if (now > lockValue) {
            return;
        }
        jedis.del(lockKey);
    }


    /**
     * @param lockKey
     * @param lockHoldDuration         锁的持有时间
     * @param lockHoldDurationTimeUnit 锁的持有时间单位
     * @return
     */
    private boolean tryLock(String lockKey, long lockHoldDuration, TimeUnit lockHoldDurationTimeUnit) {
        long lockStatus = jedis.setnx(lockKey, buildLockValue(lockHoldDuration, lockHoldDurationTimeUnit));
        if (lockStatus == 1) {
            return true;
        }
        //再次获取的时候,lock可能已经解锁
        String lockValueString = jedis.get(lockKey);
        long now = System.currentTimeMillis();
        long lockValue = Strings.isNullOrEmpty(lockValueString) ? 0 : Long.parseLong(lockValueString);
        if (lockValue >= now) {
            return false;
        }
        //锁超时了
        String getSetValueString = jedis.getSet(lockKey, buildLockValue(lockHoldDuration, lockHoldDurationTimeUnit));
        //如果A提前拿到锁，那么这一步的getset实际效果是延长了锁持有者A持有锁的时间，而本身并不会拿到锁
        long getSetValue = Strings.isNullOrEmpty(getSetValueString) ? 0 : Long.parseLong(getSetValueString);
        return getSetValue < now;
    }

    @Override
    public boolean tryLock(String lockKey, int lockEffectiveSeconds) {
        String value = buildLockValue(lockEffectiveSeconds, DEFAULT_LOCK_HOLD_TIME_UNIT);
        long lockStatus = jedis.setnx(lockKey, value);
        if (lockStatus == 1) {
            jedis.setex(lockKey, lockEffectiveSeconds, value);
            return true;
        }
        return false;
    }

    private class LockAcquisitionException extends RuntimeException {
        public LockAcquisitionException(Throwable e) {
            super(e);
        }
    }

    private class LockAcquisitionTimeoutException extends RuntimeException {
        public LockAcquisitionTimeoutException(String s) {
            super(s);
        }
    }
}
