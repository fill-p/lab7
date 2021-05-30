package buisenes_logic;

import dao.DaoException;
import dao.jdbc.JdbcPackageInfo;
import dao.jdbc.JdbcReceiveInfo;
import data.PackageInfo;
import data.ReceiveInfo;

import java.util.List;

public class ReceiveDataValidator {

    private static boolean validateReceiveInfoData(ReceiveInfo receiveInfo) throws DaoException {
        List<PackageInfo> groupList = new JdbcPackageInfo().findAll().get();

        for(PackageInfo packageInfo : groupList){
            if(packageInfo.getId() == receiveInfo.getPackageId()){
                return true;
            }
        }
        return false;
    }

    public static void createReceiveInfo(ReceiveInfo receiveInfo) throws DaoException {
        if(validateReceiveInfoData(receiveInfo)){
            new JdbcReceiveInfo().create(receiveInfo);
        } else {
            throw new DaoException("Not valid data");
        }
    }
}
