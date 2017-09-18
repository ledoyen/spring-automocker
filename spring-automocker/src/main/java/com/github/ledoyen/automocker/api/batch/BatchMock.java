package com.github.ledoyen.automocker.api.batch;

import java.util.List;

import org.springframework.batch.core.JobExecution;

public class BatchMock {

    private final JobExecution jobExecution;

    public BatchMock(JobExecution jobExecution) {
        this.jobExecution = jobExecution;

    }

    public List<Throwable> getAllFailureExceptions() {
        return jobExecution.getAllFailureExceptions();
    }
}
