package com.gameserver.utils.utils;

import com.gameserver.utils.account.entity.Account;
import com.gameserver.utils.account.entity.AccountDiscord;
import com.gameserver.utils.account.entity.Key;
import org.springframework.test.web.servlet.ResultMatcher;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestConstants {
    public static final String EMPTY_STRING = "";

    public static final int CREATED_ACCOUNT_ID = 0;

    public static final String VALID_USERNAME = "TestUser";
    public static final String SHORT_USERNAME = "us";
    public static final String LONG_USERNAME = "abcdefghijklmnopqrstuvwxyz";
    public static final String TAKEN_USERNAME = "TakenUser";

    public static final String VALID_EMAIL = "test@email.com";
    public static final String LONG_EMAIL = "a123456789123456789123456789123456789123456789@gmail.com";
    public static final String TAKEN_EMAIL = "taken@email.com";

    public static final String VALID_PASSWORD = "Abcd1234";
    public static final String SHORT_PASSWORD = "Abc";
    public static final String HASHED_PASSWORD = "$2y$12$GamT6jN.OwVtitQWQdFJD./mCOunExDrpFWHLw.b.aBYz8Mg1pN.K";

    public static final String VALID_BIRTHDAY = "1990-01-01";
    public static final String YOUNG_BIRTHDAY = (LocalDate.now()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    public static final Date BIRTHDAY_IN_MILLIS = new Date(631144800000L); // 1990-01-01

    public static final String VALID_DISCORD_ID = "100000000000000000";
    public static final String TAKEN_DISCORD_ID = "200000000000000000";
    public static final String TAKEN_DISCORD_ID2 = "300000000000000000";

    public static final int EXISTING_ACCOUNT_ID = 1234;
    public static final int EXISTING_ACCOUNT_ID2 = 5678;
    public static final Account EXISTING_ACCOUNT = new Account(TAKEN_USERNAME, TAKEN_EMAIL,
            HASHED_PASSWORD, BIRTHDAY_IN_MILLIS);
    public static final AccountDiscord EXISTING_ACCOUNT_DISCORD = new AccountDiscord(EXISTING_ACCOUNT_ID, TAKEN_DISCORD_ID);
    public static final AccountDiscord EXISTING_ACCOUNT_DISCORD2 = new AccountDiscord(EXISTING_ACCOUNT_ID2, TAKEN_DISCORD_ID2);

    public static final String VALID_SERIAL_KEY = "ABCD-1234-efgh-5678";
    public static final String INVALID_SERIAL_KEY = "1000-2000-3000-4000";
    public static final String TAKEN_SERIAL_KEY = "AAAA-BBBB-CCCC-DDDD";
    public static final Key TAKEN_KEY = new Key(2020, TAKEN_SERIAL_KEY, EXISTING_ACCOUNT_ID);

    public static final String[] INVALID_USERNAMES = new String[] {"user!", "user ", " user", "    ", "_user", "-__-"};

    public static final String[] INVALID_EMAILS = new String[] {"a", "email", "@", "@.", "abcd@", "@gmail",
            "@gmail.com", "email@example..com", "#@%^%#$@#$@#.com", "Joe Smith <email@example.com>",
            "あいうえお@example.com", "Abc..123@example.com", "email.example.com", "email@example@example.com",
            ".email@example.com", "us!er@gmail.com"};

    public static final String[] INVALID_PASSWORDS = new String[] {"abcdefg ", " abcdefg", "abcd efg",
            "       ", "______", "abc.def", "......", "[password]", "paあsword", "pássword", "pםssword"};

    public static final String[] INVALID_BIRTHDAYS = new String[] {" ", "1", "01012000", "20000101", "01 01 2000",
            "2000 01 01", "2000/01/01", "01-01-2000", "01-2000-01", "2000-13-01", "2000-01-40",
            "3000-01-01", "200-01-01", "2000-1-1", "20000-01-01", "2000-001-001", "2000-01 01",
            "2000-  -01", "2000-01-01 ", " 2000-01-01", "2000-00-01", "2000-01-0"};

    public static final String[] INVALID_SERIAL_KEY_FORMATS = new String[] {"", " ", "1234", "1234567812345678",
            " ABCD-1234-ABCD-1234", "ABCD-1234-ABCD-1234 ", "ABCD- 1234-ABCD-1234", "1234 1234 1234 1234",
            "1234_ABCD_1234_ABCD", "AAAAA-BBBB-CCCC-DDDD", "AAA-BBB-CCCC-DDDD", "AAAA-BBBB-CCCC",
            "A-B-C-D", "ABCD-1234-ABCD-12 4", "ABCD~1234-ABCD-1234", "A!CD-1234-ABCD-1234"};

    public static final String[] INVALID_DISCORD_IDS = new String[] {"", " ", "1234",
            "123456781234567812345678", " 100000000000000000", "100000000000000000 ", "1000 00000000000000",
            "abcd", "100000000000000000L", "_100000000000000000", "1000-0000-0000-0000-00",
            "100000000000000000!", "-100000000000000000"};

    public static final ResultMatcher STATUS_OK = status().isOk();
    public static final ResultMatcher STATUS_CREATED = status().isCreated();
    public static final ResultMatcher STATUS_UNAUTHORIZED = status().isUnauthorized();
    public static final ResultMatcher STATUS_FORBIDDEN = status().isForbidden();
    public static final ResultMatcher STATUS_CONFLICT = status().isConflict();
}
