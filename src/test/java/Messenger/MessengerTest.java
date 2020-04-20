package Messenger;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
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
    public void test_null_client_exception() {
        Messenger messenger = new Messenger(mockMailServer,mockEngine);
        assertThatThrownBy(() -> messenger.sendMessage(null,mockTemplate)).isInstanceOf(NullPointerException.class);
    }

    @Test public void verify_prepare_message() {
        Messenger messenger = new Messenger(mockMailServer,mockEngine);
        messenger.sendMessage(mockClient, mockTemplate);
        verify(mockEngine).prepareMessage(any(Template.class), any(Client.class));
    }

    @Test
    public void spy_test_send() {
        Messenger messenger = new Messenger(spyMailServer, spyEngine);
        when(spyEngine.prepareMessage(spyTemplate,spyClient)).thenReturn("Masz wiadomość!");
        // spy - wywoła rzeczywistą metodę, jeśli nie została nauczona
        // nie zdefiniowano stub'a z mailem, więc powinnien być null

        messenger.sendMessage(spyClient, spyTemplate);

        verify(spyEngine).prepareMessage(spyTemplate, spyClient);
        verify(spyClient).getEmail();
        verify(spyMailServer ).send(null,"Masz wiadomość!");

    }

    @Test
    public void mock_test_send() {
        Messenger messenger = new Messenger(mockMailServer, mockEngine);
        when(mockEngine.prepareMessage(mockTemplate, mockClient)).thenReturn("Masz wiadomość!");
        when(mockClient.getEmail()).thenReturn("client@gmail.com");
        // mock - musi być nauczony wszystkich wykorzystywanych metod

        messenger.sendMessage(mockClient, mockTemplate);

        verify(mockEngine).prepareMessage(mockTemplate, mockClient);
        verify(mockClient).getEmail();
        verify(mockMailServer).send("client@gmail.com", "Masz wiadomość!");
    }

    @Test
    public void captor_test_content(){
        Messenger messenger = new Messenger(mockMailServer, mockEngine);
        ArgumentCaptor<String> emailArgument = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> contentArgument = ArgumentCaptor.forClass(String.class);
        when(mockEngine.prepareMessage(any(Template.class), any(Client.class))).thenReturn("Masz wiadomość!");

        messenger.sendMessage(mockClient, mockTemplate);

        verify(mockEngine).prepareMessage(any(Template.class), any(Client.class));
        verify(mockMailServer).send(emailArgument.capture(), contentArgument.capture());
        assertThat(contentArgument.getValue()).isEqualTo("Masz wiadomość!");

    }

}
