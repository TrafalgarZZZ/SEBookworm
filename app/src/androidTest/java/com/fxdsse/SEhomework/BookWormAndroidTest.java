package com.fxdsse.SEhomework;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.fxdsse.SEhomework.data.model.User;
import com.fxdsse.SEhomework.data.service.UserService;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class BookWormAndroidTest {

    @Test
    public void testUserService() {
        String username = "__testUsername";
        String password = "__testPassword";

        Context context = InstrumentationRegistry.getTargetContext();
        User user = UserService.signUp(username, password, context);

        Assert.assertNotNull("user added", user);
        Assert.assertEquals("username saved correct", username, user.getUsername());
        Assert.assertEquals("password saved correct", new String(Hex.encodeHex(DigestUtils.md5(password))), user.getPasswordHash());

        User anotherUser = UserService.signUp(username, password, context);
        Assert.assertNull("username duplicated", anotherUser);

        User retrievedUser = UserService.login(username, password, context);
        Assert.assertNotNull("user retrieved", retrievedUser);
        Assert.assertEquals("user retrieved username correct", username, retrievedUser.getUsername());
        Assert.assertEquals("user retrieved password hash correct", new String(Hex.encodeHex(DigestUtils.md5(password))), retrievedUser.getPasswordHash());

        User deletedUser = UserService.delete(username, password, context);
        Assert.assertNotNull("user deleted", deletedUser);

        User noSuchUser = UserService.login(username, password, context);
        Assert.assertNull("no such user", noSuchUser);
    }

    @Test
    public void testOrderService() {
    }

    @Test
    public void testBookService() {
    }
}
