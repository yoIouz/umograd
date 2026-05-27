package com.umograd.presentation.subscription;

import com.umograd.domain.user.User;
import com.umograd.domain.user.UserRepository;
import com.umograd.security.AuthenticationHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subscription")
public class SubscriptionController {

    private final UserRepository userRepository;

    @GetMapping("/check-sub")
    @PreAuthorize("hasAnyRole('CHILD', 'PARENT')")
    public Map<?, ?> hasActiveSub() {
        Object authentication = AuthenticationHolder.getAuthentication().getPrincipal();
        if (!(authentication instanceof org.springframework.security.core.userdetails.User principal)) {
            return null;
        }
        Boolean isSubActive = userRepository.findByUsername(principal.getUsername())
                .map(User::isHasActiveSubscription)
                .orElseThrow();

        return Map.of("hasActiveSubscription", isSubActive);
    }

    @PostMapping("/process-payment")
    @PreAuthorize("hasAnyRole('CHILD', 'PARENT')")
    public Map<?,?> processPayment(@RequestBody Map<String, String> paymentDetails) {
        Object authentication = AuthenticationHolder.getAuthentication().getPrincipal();
        if (!(authentication instanceof org.springframework.security.core.userdetails.User principal)) {
            return null;
        }
        User user = userRepository.findByUsername(principal.getUsername())
                .orElseThrow();

        String cardNumber = paymentDetails.get("cardNumber");
        String expiry = paymentDetails.get("expiry");
        String cvc = paymentDetails.get("cvc");

        if (cardNumber == null || cardNumber.replaceAll("\\s", "").length() != 16) {
            return Map.of("error", "Некорректный номер карты");
        }
        if (expiry == null || !expiry.matches("(0[1-9]|1[0-2])/[0-9]{2}")) {
            return Map.of("error", "Неверный срок действия");
        }
        if (cvc == null || cvc.length() != 3) {
            return Map.of("error", "Неверный код CVC/CVV");
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        user.setHasActiveSubscription(true);
        userRepository.save(user);

        return Map.of("message", "Платеж успешно обработан! Годовая подписка Умоград Премиум активирована.");
    }
}
