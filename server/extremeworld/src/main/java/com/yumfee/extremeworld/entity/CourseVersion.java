package com.yumfee.extremeworld.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("revision")
public class CourseVersion extends CourseBase
{

}
