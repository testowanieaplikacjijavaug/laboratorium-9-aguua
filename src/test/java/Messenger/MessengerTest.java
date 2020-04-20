package Messenger;

import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class MessengerTest {

    //MOCK
    Template mockTemplate = mock(Template.class);
    Client mockClient = mock(Client.class);
    MailServer mockMailServer = mock(MailServer.class);
    TemplateEngine mockEngine = mock(TemplateEngine.class);

    //SPY
    Template spyTemplate = spy(Template.class);
    Client spyClient = spy(Client.class);
    MailServer spyMailServer = spy(MailServer.class);
    TemplateEngine spyEngine = spy(TemplateEngine.class);


    @Test
    public void mock_test_send() {
        Messenger messenger = new Messenger(mockMailServer,mockEngine);
        when(mockEngine.prepareMessage(mockTemplate, mockClient)).thenReturn("Masz wiadomość!");
        when(mockClient.getEmail()).thenReturn("client@gmail.com");

        messenger.sendMessage(mockClient, mockTemplate);

        verify(mockEngine).prepareMessage(mockTemplate, mockClient);
        verify(mockClient).getEmail();
        verify(mockMailServer).send("client@gmail.com", "Masz wiadomość!");
    }

    @Test
    public void spy_test_send() {
        Messenger messenger = new Messenger(spyMailServer ,spyEngine);
        when(spyEngine.prepareMessage(spyTemplate,spyClient)).thenReturn("Masz wiadomość!");
        when(spyClient.getEmail()).thenReturn("spy@gmail.com");

        messenger.sendMessage(spyClient, spyTemplate);

        verify(spyEngine).prepareMessage(spyTemplate, spyClient);
        verify(spyClient).getEmail();
        verify(spyMailServer ).send("spy@gmail.com","Masz wiadomość!");

    }

    @Test
    public void nullClientShouldThrowsExceptionTest() {
        Messenger messenger = new Messenger(mockMailServer,mockEngine);
        assertThatThrownBy(() -> messenger.sendMessage(null,mockTemplate)).isInstanceOf(NullPointerException.class);
    }






}
