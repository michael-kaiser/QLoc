package com.example.qloc.model.ThreadControl;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by michael on 11.05.15.
 */
public class NetworkExecuter<T> {
    private ExecutorService executer =  Executors.newSingleThreadExecutor();
    private Future<T> result;

    public void execute(Callable<T> call){
        result = executer.submit(call);
    }
    //ToDo check for queue empty
    public T getResultWait(long millis) throws InterruptedException, ExecutionException {
        T response;
        try{
            response = result.get(millis, TimeUnit.MILLISECONDS);
        }catch(TimeoutException e){
            return null;
        }
        return response;
    }

    public T getResult() throws InterruptedException, ExecutionException{
        return result.get();
    }


}
