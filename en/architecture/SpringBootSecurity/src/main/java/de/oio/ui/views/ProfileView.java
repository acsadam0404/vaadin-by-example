package de.oio.ui.views;

import com.google.common.eventbus.Subscribe;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import de.oio.ui.MainUI;
import de.oio.ui.components.GoToMainViewLink;
import de.oio.ui.components.LogoutLink;
import de.oio.ui.events.UserLoggedInEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;

import javax.annotation.PostConstruct;

@Secured("ROLE_USER")
@SpringView(name = ProfileView.NAME)
public class ProfileView extends AbstractView {

    public final static String NAME = "profile";
    private Logger LOG = LoggerFactory.getLogger(ProfileView.class);
    private LogoutLink logoutLink;
    private Label label;

    public ProfileView() {
        logoutLink = new LogoutLink();
        label = new Label("", ContentMode.HTML);
        updateLabelProperty();
        addComponent(label);
        addComponent(new GoToMainViewLink());
        addComponent(logoutLink);
        logoutLink.updateVisibility();

        registerWithEventbus();
    }

    @PostConstruct
    public void postConstruct() {
        LOG.info("Created new instance of ProfileView");
    }

    private void updateLabelProperty() {
        label.setValue("<h1>"
                + (MainUI.getCurrent().getCurrentUser() == null ? "" : MainUI.getCurrent().getCurrentUser().getFullName())
                + "'s Profile</h1>... not much to see here, though.");
    }

    @Override
    public void enter(ViewChangeEvent event) {
        LOG.info("Entering profile view");
    }

    @Subscribe
    public void userLoggedIn(UserLoggedInEvent event) {
        logoutLink.updateVisibility();
        updateLabelProperty();
    }
}
