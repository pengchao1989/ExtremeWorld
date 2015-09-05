package com.jixianxueyuan.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pengchao on 6/30/15.
 */
public class HandshakeDTO implements Serializable {
    private List<HobbyDTO> hobbys;

    public List<HobbyDTO> getHobbyDTOList() {
        return hobbys;
    }

    public void setHobbyDTOList(List<HobbyDTO> hobbys) {
        this.hobbys = hobbys;
    }
}
