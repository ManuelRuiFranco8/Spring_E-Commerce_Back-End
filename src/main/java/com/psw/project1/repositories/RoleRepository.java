package com.psw.project1.repositories;

import com.psw.project1.utils.enums.User_Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.*;
import com.psw.project1.entities.*;
import java.util.*;

@Repository
public interface RoleRepository extends JpaRepository<Role, User_Roles> {

    List<Role> findByType(User_Roles type); //returns a single element list ("type" is key for the entity Role)
}//RoleRepository