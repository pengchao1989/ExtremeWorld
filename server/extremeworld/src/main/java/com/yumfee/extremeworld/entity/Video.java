package com.yumfee.extremeworld.entity;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

//type=4  typec="video"

@Entity
@DiscriminatorValue("4")
public class Video extends Topic
{
	
}
