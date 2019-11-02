package com.pos.ecommerce.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.pos.ecommerce.client.dto.UserDTO;

import java.util.List;

/**
 * Created by michalvlcek on 18.03.15.
 */
@RemoteServiceRelativePath("UserService")
public interface UserService extends RemoteService {

    public List<UserDTO> getUsers();

    public Long saveUser(UserDTO user);

    public void saveBorrow(Long userId, Long carId);
}
