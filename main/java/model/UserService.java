package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dao.UserDao;

public class UserService {

    private final UserDao userDao = new UserDao();

    public boolean authenticate(String userId, String password) {
        String id = trim(userId);
        String pass = password == null ? null : password;
        if (id == null || id.isBlank() || pass == null || pass.isBlank()) {
            return false;
        }
        return userDao.login(id, pass);
    }

    public RegisterResult register(String rawUserId, String rawPassword, String rawConfirm) {
        String userId = trim(rawUserId);
        String password = rawPassword == null ? null : rawPassword.trim();
        String confirm = rawConfirm == null ? null : rawConfirm.trim();

        List<String> errors = new ArrayList<>();
        validateUserId(userId, errors);
        validatePassword(password, confirm, errors);

        if (errors.isEmpty() && userDao.exists(userId)) {
            errors.add("既に同じユーザーIDが登録されています");
        }

        if (!errors.isEmpty()) {
            return RegisterResult.failure(userId, errors);
        }

        if (!userDao.register(userId, password)) {
            errors.add("登録に失敗しました。もう一度お試しください");
            return RegisterResult.failure(userId, errors);
        }

        return RegisterResult.success(userId);
    }

    private void validateUserId(String userId, List<String> errors) {
        if (userId == null || userId.isBlank()) {
            errors.add("ユーザーIDを入力してください");
        } else if (userId.length() > 32) {
            errors.add("ユーザーIDは32文字以内で入力してください");
        }
    }

    private void validatePassword(String password, String confirm, List<String> errors) {
        if (password == null || password.isBlank()) {
            errors.add("パスワードを入力してください");
        } else if (password.length() < 4) {
            errors.add("パスワードは4文字以上で入力してください");
        }

        if (confirm == null || confirm.isBlank()) {
            errors.add("確認用パスワードを入力してください");
        } else if (password != null && !password.equals(confirm)) {
            errors.add("パスワードが一致しません");
        }
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }

    public static final class RegisterResult {
        private final boolean success;
        private final String userId;
        private final List<String> errors;

        private RegisterResult(boolean success, String userId, List<String> errors) {
            this.success = success;
            this.userId = userId;
            this.errors = Collections.unmodifiableList(new ArrayList<>(errors));
        }

        public static RegisterResult success(String userId) {
            return new RegisterResult(true, userId, List.of());
        }

        public static RegisterResult failure(String userId, List<String> errors) {
            return new RegisterResult(false, userId, errors);
        }

        public boolean isSuccess() {
            return success;
        }

        public String getUserId() {
            return userId;
        }

        public List<String> getErrors() {
            return errors;
        }
    }
}
