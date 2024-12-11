package com.batman.listener;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import com.batman.entity.Discount;
import com.batman.events.DiscountCreatedEvent;
import com.batman.jobs.ActivateDiscountJob;
import com.batman.jobs.DeActivateDiscountJob;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class DiscountEventListener {

	private final SchedulerFactoryBean schedulerFactoryBean;

	@EventListener
	public void handleDiscountCreatedEvent(DiscountCreatedEvent discountCreatedEvent) throws SchedulerException {
		log.info("Entering Handle Discount Created Event ... ");
		if (Objects.isNull(discountCreatedEvent.getDiscount())) {
			log.error("Discount is Null in Event ...");
			return;
		}
		Discount discount = discountCreatedEvent.getDiscount();
		log.info("Discount Id :: {}", discount.getDiscountId());

		Date scheduledStartJobDate = scheduleActivateDiscountTask(discount);
		log.info("Activation Scheduled Date :: {}", scheduledStartJobDate.toString());
		Date scheduledEndJobDate = scheduleDeActivateDiscountTask(discount);
		log.info("De-Activation Scheduled Date :: {}", scheduledEndJobDate.toString());
		log.info("Leaving Handle Discount Created Event ... ");
	}

	private Date scheduleActivateDiscountTask(Discount discount) throws SchedulerException {
		log.info("Entering Schedule Activate Discount Task ...");
		JobDetail activateDiscountJobDetail = JobBuilder.newJob(ActivateDiscountJob.class)
				.usingJobData("discountId", discount.getDiscountId()).storeDurably().build();

		LocalDateTime startDateAndTime = LocalDateTime.of(discount.getFromDate(), discount.getFromTime());

		Date scheduleStartDate = Date.from(startDateAndTime.atZone(ZoneId.systemDefault()).toInstant());
//        Date scheduleStartDate = Date.from(startDateAndTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC).toInstant());
		Trigger activateDiscountTrigger = TriggerBuilder.newTrigger().startAt(scheduleStartDate).build();
		log.info("Discount activation trigger start date :: {}", scheduleStartDate);

		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		scheduler.start();
		Date scheduledJobDate = scheduler.scheduleJob(activateDiscountJobDetail, activateDiscountTrigger);
		log.info("Leaving Schedule Activate Discount Task ...");
		return scheduledJobDate;
	}

	private Date scheduleDeActivateDiscountTask(Discount discount) throws SchedulerException {
		log.info("Entering Schedule DeActivate Discount Task ...");
		JobDetail deActivateDiscountJobDetail = JobBuilder.newJob(DeActivateDiscountJob.class)
				.usingJobData("discountId", discount.getDiscountId()).storeDurably().build();

		LocalDateTime endDateAndTime = LocalDateTime.of(discount.getToDate(), discount.getToTime());

		Date scheduleStartDate = Date.from(endDateAndTime.atZone(ZoneId.systemDefault()).toInstant());

		Trigger deActivateDiscountTrigger = TriggerBuilder.newTrigger().startAt(scheduleStartDate).build();
		log.info("Discount activation trigger start date :: {}", scheduleStartDate);

		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		scheduler.start();
		Date scheduledJobDate = scheduler.scheduleJob(deActivateDiscountJobDetail, deActivateDiscountTrigger);
		log.info("Leaving Schedule DeActivate Discount Task ...");
		return scheduledJobDate;
	}


}
