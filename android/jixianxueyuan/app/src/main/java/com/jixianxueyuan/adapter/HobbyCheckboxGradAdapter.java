package com.jixianxueyuan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.jixianxueyuan.R;
import com.jixianxueyuan.app.AppInfomation;
import com.jixianxueyuan.dto.HobbyDTO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by pengchao on 6/2/16.
 */
public class HobbyCheckboxGradAdapter extends BaseAdapter {

    private Context context;
    private List<HobbyDTO> hobbyDTOList;
    private Set<HobbyDTO> selectedHobbySets;

    public HobbyCheckboxGradAdapter(Context context){
        this.context = context;
        hobbyDTOList = new ArrayList<HobbyDTO>();
        selectedHobbySets = new HashSet<HobbyDTO>();
        if (AppInfomation.getInstance().getHandshakeDTO() != null){
            List<HobbyDTO> allHobbys = AppInfomation.getInstance().getHandshakeDTO().getHobbyDTOList();
            selectedHobbySets.add(AppInfomation.getInstance().getCurrentHobbyInfo());

            for (HobbyDTO hobbyDTO : allHobbys){
                if (hobbyDTO.getId() != HobbyDTO.ALL_HOBBY_ID){
                    hobbyDTOList.add(hobbyDTO);
                }
            }
        }
    }

    public Set<HobbyDTO> getSelectedHobbySets(){
        return selectedHobbySets;
    }

    @Override
    public int getCount() {
        return hobbyDTOList.size();
    }

    @Override
    public Object getItem(int position) {
        return hobbyDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.hobby_checkbox_grad_item, null);
        }
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.hobby_checkbox_item);

        final HobbyDTO hobbyDTO = hobbyDTOList.get(position);
        checkBox.setText(hobbyDTO.getName());
        if (selectedHobbySets.contains(hobbyDTO)){
            checkBox.setChecked(true);
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    selectedHobbySets.add(hobbyDTO);
                }else {
                    selectedHobbySets.remove(hobbyDTO);
                }
            }
        });

        return convertView;
    }
}
