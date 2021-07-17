package net.minecraftcapes.helpers;

import com.google.common.collect.Queues;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import lombok.Getter;
import org.apache.commons.lang3.Validate;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class ScheduleTask {

    @Getter private static ScheduleTask instance;
    private final Queue<FutureTask<? >> scheduledTasks = Queues. < FutureTask<? >> newArrayDeque();

    public ScheduleTask() {
        ScheduleTask.instance = this;
    }

    public ListenableFuture<Object> addScheduledTask(Runnable runnableToSchedule) {
        Validate.notNull(runnableToSchedule);
        return this.<Object>addScheduledTask(Executors.callable(runnableToSchedule));
    }

    public <V> ListenableFuture<V> addScheduledTask(Callable<V> callableToSchedule) {
        Validate.notNull(callableToSchedule);
        ListenableFutureTask<V> listenablefuturetask = ListenableFutureTask.<V>create(callableToSchedule);

        synchronized (this.scheduledTasks)
        {
            this.scheduledTasks.add(listenablefuturetask);
            return listenablefuturetask;
        }
    }

    @SubscribeEvent
    public void onRender(TickEvent.RenderTickEvent event) {
        while(!this.scheduledTasks.isEmpty()) {
            FutureTask task = this.scheduledTasks.poll();
            task.run();
        }
    }
}