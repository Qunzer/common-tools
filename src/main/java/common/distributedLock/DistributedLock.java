package common.distributedLock;

/**
 * Created by rq on 2017/04/17.
 */
public interface DistributedLock {

    /**
     * @param lockKey
     * @return true:代表获得锁；
     */
    boolean tryLock(String lockKey);

    void unlock(String lockKey);

    /**
     * @param lockKey
     * @param lockEffectiveSeconds 锁的有效时间
     * @return
     */
    boolean tryLock(String lockKey, int lockEffectiveSeconds);

    void lock(String lockKey);

    /**
     * @param lockKey
     * @param acquireLockWaitSeconds 获取锁的超时时间
     */
    void lock(String lockKey, long acquireLockWaitSeconds);

    /**
     * @param lockKey
     * @param lockAcquireWaitSeconds  获取锁的超时时间
     * @param lockHoldDurationSeconds 持有锁的超时时间
     */
    void lock(String lockKey, long lockAcquireWaitSeconds, long lockHoldDurationSeconds);
}
