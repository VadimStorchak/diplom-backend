package kubsu.ru.diplombackend.services;

import kubsu.ru.diplombackend.entity.User;
import kubsu.ru.diplombackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void onStart() {
        List<User> all = userRepository.findAll();
        if (CollectionUtils.isEmpty(all)) {
            userRepository.save(User.builder()
                    .fio("Test FIO")
                    .secret(UUID.randomUUID().toString())
                    .build());
        }
    }

    public User getUser() {
        List<User> all = userRepository.findAll();
        if (CollectionUtils.isEmpty(all)) {
            return userRepository.save(User.builder()
                    .fio("Test FIO")
                    .secret(UUID.randomUUID().toString())
                    .build());
        }
        return all.get(0);
    }
}
