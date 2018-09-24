package com.odde.securetoken;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuthenticationServiceTest {

    Profile stubProfile = mock(Profile.class);
    MyToken stubToken = mock(MyToken.class);
    MyLogger mockLogger = mock(MyLogger.class);

    AuthenticationService target = new AuthenticationService(stubProfile, stubToken, mockLogger);

    @Test
    public void is_valid_test() {
        givenProfile("joey", "91");
        givenToken("000000");

        shouldBeValid("joey", "91000000");
    }

    @Test
    public void should_log_account_when_invalid() {
        whenInvalid();
        shouldLogWithKeyWords("joey", "login failed");
    }

    @Test
    public void should_not_log_account_when_valid() {
        whenValid();
        shouldNotLogAnything();
    }

    private void shouldNotLogAnything() {
        verify(mockLogger, never()).save(anyString());
    }

    private void whenValid() {
        givenProfile("joey", "91");
        givenToken("000000");
        target.isValid("joey", "91000000");
    }


    private void whenInvalid() {
        givenProfile("joey", "91");
        givenToken("000000");
        target.isValid("joey", "wrong password");
    }

    private void shouldLogWithKeyWords(String account, String action) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mockLogger).save(captor.capture());

        String message = captor.getValue();
        assertThat(message).contains(account, action);
    }

    private void shouldBeValid(String account, String password) {
        assertTrue(target.isValid(account, password));
    }

    private void givenToken(String random) {
        when(stubToken.getRandom(anyString())).thenReturn(random);
    }

    private void givenProfile(String account, String password) {
        when(stubProfile.getPassword(account)).thenReturn(password);
    }

}

