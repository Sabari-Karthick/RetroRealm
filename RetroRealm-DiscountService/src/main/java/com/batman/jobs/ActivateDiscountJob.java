package com.batman.jobs;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.batman.entity.Discount;
import com.batman.service.IDiscountService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ActivateDiscountJob implements Job {

	@Autowired
	private  IDiscountService discountService;
	
	

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("Entering Activate Discount Job Execute ...");
	    log.info("Job ** {} ** fired @ {}", context.getJobDetail().getKey().getName(), context.getFireTime());


		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		Integer discountId = (Integer) jobDataMap.get("discountId");

		log.info("Executing Activating Discount Job for discount with ID {} ...", discountId);

		Discount discount = discountService.activateDiscount(discountId);

		log.info("Discount :: {}", discount);
		log.info("Leaving Activate Discount job Execute ...");
	}

}
