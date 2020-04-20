package Example;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FriendshipMongoTestMockito {

    @Mock
    private FriendsCollection friends;

    @InjectMocks
    private FriendshipsMongo friendships;

    @Test
    public void alexDoesNotHaveFriends(){
        assertThat(friendships.getFriendsList("Alex")).isEmpty();
    }

    @Test
    public void joeAndAlexAreNotFriends(){
        assertThat(friendships.areFriends("Joe", "Alex")).isFalse();
    }


    @Test
    public void joeAndAlexAreFriends() {
        Person joe = mock(Person.class);
        when(friends.findByName("Joe")).thenReturn(joe);
        when(joe.getFriends()).thenReturn(Collections.singletonList("Alex"));
        assertThat(friendships.areFriends("Joe", "Alex")).isTrue();
    }

    @Test public void joeAndAlexAreFriendsVerifyOtherMethodCalled(){
        Person joe = mock(Person.class);
        when(friends.findByName("Joe")).thenReturn(joe);
        when(joe.getFriends()).thenReturn(Collections.singletonList("Alex"));
        friendships.areFriends("Joe", "Alex");
        verify(friends).findByName("Joe");
        verify(joe).getFriends();
    }

    @Test
    public void getName(){
        Person joe = mock(Person.class);
        when(joe.getName()).thenReturn("Joe");
        assertThat(joe.getName()).isEqualTo("Joe");
    }
    @Test
    public void addFriends() {
        Person joe = mock(Person.class);
        List<String> friendsList = new ArrayList<String>();

        doAnswer((i) -> {
            friendsList.add(i.getArgument(0));
            return null;
        }).when(joe).addFriend(anyString());
        when(friends.findByName("Joe")).thenReturn(joe);
        when(joe.getFriends()).thenReturn(friendsList);

        joe.addFriend("Alex");
        joe.addFriend("Max");

        assertThat(friendships.getFriendsList("Joe")).hasSize(2).containsOnly("Alex", "Max");
        verify(friends).findByName("Joe");
        verify(joe).getFriends();
    }

    @Test public void testMakeFriends(){
        friendships.makeFriends("Alex", "Joe");
        verify(friends, times(2)).save(any(Person.class));
    }

}
