package com.jixianxueyuan.dto;

import java.util.List;

/**
 * Created by pengchao on 6/30/15.
 */
public class BaseInfoDTO {
    List<HobbyDTO> hobbys;

    public List<HobbyDTO> getHobbyDTOList() {
        return hobbys;
    }

    public void setHobbyDTOList(List<HobbyDTO> hobbys) {
        this.hobbys = hobbys;
    }
}
