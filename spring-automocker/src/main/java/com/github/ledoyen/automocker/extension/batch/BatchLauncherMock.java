package com.github.ledoyen.automocker.extension.batch;

import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class BatchLauncherMock {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private Map<String, Job> jobsByName;

	public BatchMock launch(String name) throws Exception {
		Job job = jobsByName.get(name);
		JobLauncherTestUtils testWrapper = new JobLauncherTestUtils();
		testWrapper.setJobRepository(jobRepository);
		testWrapper.setJobLauncher(jobLauncher);
		testWrapper.setJob(job);
		return new BatchMock(testWrapper.launchJob());
	}
}
