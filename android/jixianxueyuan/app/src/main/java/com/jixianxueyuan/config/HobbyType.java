package com.jixianxueyuan.config;

/**
 * Created by pengchao on 7/18/15.
 */
public class HobbyType {
    public static final String STRING = "hobbyType";

    public static final String ALL = "all";

    public static final String SKATEBOARD = "skateboard";
    public static final String PARKOUR = "parkour";
    public static final String BMX = "bmx";
    public static final String ROLLER_SKATING = "roller_skating";
    public static final String SKEE = "skee";


    public static Long getHobbyId(String hobby)
    {
        Long hobbyId = 0L;

        if(hobby == null)
        {
            hobbyId = 0L;
        }
        else if(hobby.equals(ALL))
        {
            hobbyId = 0L;
        }
        else if(hobby.equals(SKATEBOARD))
        {
            hobbyId = 1L;
        }
        else if(hobby.equals(PARKOUR))
        {
            hobbyId = 2L;
        }
        else if(hobby.equals(BMX))
        {
            hobbyId = 3L;
        }
        else if(hobby.equals("roller-skating"))
        {
            hobbyId = 4L;
        }
        else if(hobby.equals("snowboard"))
        {
            hobbyId = 5L;
        }

        return hobbyId;
    }
}
