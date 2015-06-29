package com.yumfee.extremeworld.entity;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

//type=5  typec="activity"

@Entity
@DiscriminatorValue("6")
public class Activity extends Topic
{
	private ActivityDetail activityDetail;

	@OneToOne(cascade={CascadeType.ALL})
	@JoinColumn(name="id")
	public ActivityDetail getActivityDetail()
	{
		return activityDetail;
	}

	public void setActivityDetail(ActivityDetail activityDetail)
	{
		this.activityDetail = activityDetail;
	}
	
	
}
