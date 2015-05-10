package com.yumfee.extremeworld.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

//type=6 typec="news"
@Entity
@DiscriminatorValue("6")
public class News extends Topic{

}
