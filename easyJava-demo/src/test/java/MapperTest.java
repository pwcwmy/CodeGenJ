import com.easyjava.RunDemoApplication;
import com.easyjava.entity.po.UserInfo;
import com.easyjava.entity.query.UserInfoQuery;
import com.easyjava.mappers.UserInfoMapper;
import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;

@SpringBootTest(classes = RunDemoApplication.class)
@RunWith(SpringRunner.class)
public class MapperTest {

    @Resource
    private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

   /* @Test
    public void selectList() {
        List<UserInfo> userInfoList = userInfoMapper.selectList(new UserInfoQuery());
        System.out.println(userInfoList);
    }

    @Test
    public void selectCount() {
        Long cnt = userInfoMapper.selectCount(new UserInfoQuery());
        System.out.println(cnt);
    }

    @Test
    public void insert() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId("8");
        userInfo.setNickname("8");
        userInfo.setPassword("22");
        userInfo.setEmail("2228");
        userInfo.setJoinTime(new Date());
        userInfo.setTotalCoinCount(10);
        userInfo.setCurrentCoinCount(10);
        System.out.println(userInfoMapper.insert(userInfo));
    }

    @Test
    public void insertOrUpdate() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId("331343");
        userInfo.setNickname("232");
        userInfo.setPassword("22");
        userInfo.setEmail("22223");
        userInfo.setJoinTime(new Date());
        userInfo.setTotalCoinCount(10);
        userInfo.setCurrentCoinCount(10);
        System.out.println(userInfoMapper.insertOrUpdate(userInfo));
    }

    @Test
    public void insertBatch() {
        List<UserInfo> list = new ArrayList<>();
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId("23");
        userInfo.setNickname("2");
        userInfo.setPassword("22");
        userInfo.setEmail("2223");
        userInfo.setJoinTime(new Date());
        userInfo.setTotalCoinCount(10);
        userInfo.setCurrentCoinCount(10);
        userInfo.setStatus(1);
        list.add(userInfo);
        userInfo.setTheme(1);
        System.out.println(userInfoMapper.insertBatch(list));
    }

    @Test
    public void insertOrUpdateBatch() {
        List<UserInfo> list = new ArrayList<>();
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId("331343");
        userInfo.setNickname("232");
        userInfo.setPassword("22");
        userInfo.setEmail("22223");
        userInfo.setJoinTime(new Date());
        userInfo.setTotalCoinCount(10);
        userInfo.setCurrentCoinCount(10);
        userInfo.setStatus(1);
        userInfo.setTheme(1);
        list.add(userInfo);
        System.out.println(userInfoMapper.insertOrUpdateBatch(list));
    }

    @Test
    public void selectByUserId() {
        System.out.println(userInfoMapper.selectByUserId("2"));
    }

    @Test
    public void selectByEmail() {
        System.out.println(userInfoMapper.selectByEmail("222"));
    }

    @Test
    public void selectByNickname() {
        System.out.println(userInfoMapper.selectByNickname("222"));
    }*/

   /* @Test
    @Order(1)
    public void updateByUserId() {
        UserInfo userInfo = new UserInfo();
        userInfo.setTheme(2);
        System.out.println(userInfoMapper.updateByUserId(userInfo, "2"));
    }

    @Test
    @Order(2)
    public void updateByEmail() {
        UserInfo userInfo = new UserInfo();
        userInfo.setTheme(2);
        System.out.println(userInfoMapper.updateByEmail(userInfo, "222"));
    }

    @Test
    @Order(3)
    public void updateByNickname() {
        UserInfo userInfo = new UserInfo();
        userInfo.setTheme(2);
        System.out.println(userInfoMapper.updateByNickname(userInfo, "222"));
    }

    @Test
    @Order(4)
    public void deleteByUserId() {
        System.out.println(userInfoMapper.deleteByUserId("2"));
    }

    @Test
    @Order(5)
    public void deleteByEmail() {
        System.out.println(userInfoMapper.deleteByEmail("222"));
    }

    @Test
    @Order(6)
    public void deleteByNickname() {
        System.out.println(userInfoMapper.deleteByNickname("222"));
    }
*/
    @Test
    public void insert() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId("899");
        userInfo.setNickname("899");
        userInfo.setPassword("22");
        userInfo.setEmail("222899");
        userInfo.setJoinTime(new Date());
        userInfo.setTotalCoinCount(10);
        userInfo.setCurrentCoinCount(10);
        System.out.println(userInfoMapper.insert(userInfo));
    }

}
