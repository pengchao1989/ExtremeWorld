package com.yumfee.extremeworld.entity;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@DiscriminatorValue("2")
public class Activity extends Topic
{
	private ActivityDetail activityDetail;

	@OneToOne(cascade={CascadeType.ALL})
	@JoinColumn(name="activity_id")
	public ActivityDetail getActivityDetail()
	{
		return activityDetail;
	}

	public void setActivityDetail(ActivityDetail activityDetail)
	{
		this.activityDetail = activityDetail;
	}
	
	
}
