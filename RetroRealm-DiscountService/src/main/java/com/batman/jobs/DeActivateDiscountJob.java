package com.batman.jobs;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.batman.entity.Discount;
import com.batman.service.IDiscountService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DeActivateDiscountJob implements Job{
	
	@Autowired
	private IDiscountService discountService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("Entering De-Activate Discount Execute ...");
		log.info("Job ** {} ** fired @ {}", context.getJobDetail().getKey().getName(), context.getFireTime());
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		Integer discountId = (Integer)jobDataMap.get("discountId");
		log.info("Executing De-Activating Discount Job for discount with ID {} ...",discountId);
		Discount discount = discountService.deActivateDiscount(discountId);
		
		log.info("Discount :: {}",discount);
		
		log.info("Leaving De-Activate Discount Execute ...");
	}

}
