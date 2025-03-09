package com.arom.yeojung.service;

import com.arom.yeojung.object.User;
import com.arom.yeojung.object.dto.AuthDto;
import com.arom.yeojung.repository.UserRepository;
import com.arom.yeojung.util.exception.CustomException;
import com.arom.yeojung.util.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void join(AuthDto authDto){

        String username = authDto.getUsername();
        String password = authDto.getPassword();
        String nickname = authDto.getNickname();

        Boolean isExist = userRepository.existsByUsername(username);

        if (isExist){
            throw new CustomException(ErrorCode.EXIST_USER);
        }

        User data = new User();

        data.setUsername(username);
        data.setPassword(bCryptPasswordEncoder.encode(password));
        data.setNickname(nickname);

        userRepository.save(data);
    }
}
