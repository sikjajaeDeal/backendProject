package likelion.beanBa.backendProject.member.email.service;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EmailAuthService {

    private final Set<String> verifiedEmails = ConcurrentHashMap.newKeySet();

    public void markEmailAsVerified(String email) {
        verifiedEmails.add(email);
    }

    public boolean isEmailVerified(String email){
        return verifiedEmails.contains(email);
    }

    public void clearVerified(String email) {
        verifiedEmails.remove(email);
    }

}
