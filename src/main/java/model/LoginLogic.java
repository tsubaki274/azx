package model;

import java.util.ArrayList;
import java.util.List;

import dao.UserDAO;

public class LoginLogic {
    private final UserDAO userDAO = new UserDAO();

    public boolean authenticate(String userId, String password) {
        if (userId == null || password == null) {
            return false;
        }
        return userDAO.authenticate(userId.trim(), password);
    }

    public List<String> validateRegistration(String userId, String password, String passwordConfirm) {
        List<String> errors = new ArrayList<>();
        String normalizedUserId = userId == null ? "" : userId.trim();
        if (normalizedUserId.isEmpty()) {
            errors.add("ユーザーIDを入力してください。");
        } else {
            if (!normalizedUserId.matches("[a-zA-Z0-9_]{3,10}")) {
                errors.add("ユーザーIDは3〜10文字の半角英数字にしてください。");
            }
        }

        if (password == null || password.isBlank()) {
            errors.add("パスワードを入力してください。");
        } else if (password.length() < 4 || password.length() > 10) {
            errors.add("パスワードは4〜10文字にしてください。");
        }

        if (passwordConfirm == null || !passwordConfirm.equals(password)) {
            errors.add("パスワード（確認）と一致しません。");
        }

        if (errors.isEmpty() && userDAO.exists(normalizedUserId)) {
            errors.add("このユーザーIDは既に登録されています。");
        }

        return errors;
    }

    public boolean register(String userId, String password) {
        return userDAO.register(userId.trim(), password);
    }
}
