package com.sh3h.workmanagerjm.util;

import com.sh3h.dataprovider.data.entity.ui.DUPerson;
import com.sh3h.dataprovider.data.entity.ui.DUSearchPerson;
import com.sh3h.dataprovider.data.entity.ui.DUWord;
import com.sh3h.dataprovider.util.ConstantUtil;
import com.sh3h.mobileutil.util.TextUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiMeng on 2018/2/28.
 */

public class DispatchUtil {

    private List<DUWord> difficultList, driverList;
    private List<DUSearchPerson> acceptPersons, assistPersons;

    public List<DUSearchPerson> provideAcceptPerson(List<DUPerson> list){
        if (acceptPersons != null && acceptPersons.size() > 0){
            return acceptPersons;
        }

        acceptPersons = new ArrayList<>();
        if (list == null || list.size() == 0){
            return acceptPersons;
        }

        String roles;
        for (DUPerson person : list){
            roles = person.getRoles();
            if (TextUtil.isNullOrEmpty(roles)){
                continue;
            }

            if (roles.contains(ConstantUtil.PersonRole.METER_TASK)){
                acceptPersons.add(new DUSearchPerson(person.getName(), person.getAccount()));
            }
        }
        return acceptPersons;
    }

    public List<DUWord> provideDifficult(){
        if (difficultList != null){
            return difficultList;
        }

        difficultList = new ArrayList<>();
        difficultList.add(new DUWord(String.valueOf(0)));
        difficultList.add(new DUWord(String.valueOf(0.5)));
        difficultList.add(new DUWord(String.valueOf(1)));
        difficultList.add(new DUWord(String.valueOf(1.5)));
        difficultList.add(new DUWord(String.valueOf(2)));
        difficultList.add(new DUWord(String.valueOf(3)));
        difficultList.add(new DUWord(String.valueOf(5)));
        difficultList.add(new DUWord(String.valueOf(6)));
        difficultList.add(new DUWord(String.valueOf(8)));
        difficultList.add(new DUWord(String.valueOf(10)));
        difficultList.add(new DUWord(String.valueOf(15)));
        difficultList.add(new DUWord(String.valueOf(20)));
        difficultList.add(new DUWord(String.valueOf(30)));
        difficultList.add(new DUWord(String.valueOf(40)));
        difficultList.add(new DUWord(String.valueOf(45)));
        difficultList.add(new DUWord(String.valueOf(50)));
        difficultList.add(new DUWord(String.valueOf(75)));
        return difficultList;
    }

    public List<DUWord> provideDriver(List<DUPerson> list){
        if (driverList != null && driverList.size() > 0){
            return driverList;
        }

        driverList = new ArrayList<>();
        driverList.add(new DUWord(TextUtil.EMPTY, TextUtil.EMPTY));
        if (list == null || list.size() == 0){
            return driverList;
        }

        String roles;
        for (DUPerson person : list){
            roles = person.getRoles();
            if (TextUtil.isNullOrEmpty(roles)){
                continue;
            }

            if (roles.contains(ConstantUtil.PersonRole.DRIVER)){
                driverList.add(new DUWord(person.getName(), person.getAccount()));
            }
        }
        return driverList;
    }

    public List<DUSearchPerson> provideAssistPerson(List<DUPerson> list){
        if (assistPersons != null && assistPersons.size() > 0){
            return assistPersons;
        }

        assistPersons = new ArrayList<>();
        if (list == null || list.size() == 0){
            return assistPersons;
        }

        String roles;
        for (DUPerson person : list){
            roles = person.getRoles();
            if (TextUtil.isNullOrEmpty(roles)){
                continue;
            }

            if (roles.contains(ConstantUtil.PersonRole.METER_TASK)){
                assistPersons.add(new DUSearchPerson(person.getName(), person.getAccount()));
            }
        }
        return assistPersons;
    }

}
