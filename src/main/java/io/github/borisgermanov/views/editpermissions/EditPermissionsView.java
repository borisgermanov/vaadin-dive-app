package io.github.borisgermanov.views.editpermissions;

import com.vaadin.collaborationengine.CollaborationAvatarGroup;
import com.vaadin.collaborationengine.CollaborationBinder;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import io.github.borisgermanov.data.UserPermission;
import io.github.borisgermanov.services.UserPermissionService;
import jakarta.annotation.security.RolesAllowed;
import java.util.Optional;
import java.util.UUID;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Edit Permissions")
@Route("edit-permissions/:userPermissionID?/:action?(edit)")
@Menu(order = 1, icon = LineAwesomeIconUrl.COLUMNS_SOLID)
@RolesAllowed("ADMIN")
public class EditPermissionsView extends Div implements BeforeEnterObserver {

    private final String USERPERMISSION_ID = "userPermissionID";
    private final String USERPERMISSION_EDIT_ROUTE_TEMPLATE = "edit-permissions/%s/edit";

    private final Grid<UserPermission> grid = new Grid<>(UserPermission.class, false);

    CollaborationAvatarGroup avatarGroup;

    private TextField loginName;
    private TextField fullName;
    private TextField permission;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final CollaborationBinder<UserPermission> binder;

    private UserPermission userPermission;

    private final UserPermissionService userPermissionService;

    public EditPermissionsView(UserPermissionService userPermissionService) {
        this.userPermissionService = userPermissionService;
        addClassNames("edit-permissions-view");

        // UserInfo is used by Collaboration Engine and is used to share details
        // of users to each other to able collaboration. Replace this with
        // information about the actual user that is logged, providing a user
        // identifier, and the user's real name. You can also provide the users
        // avatar by passing an url to the image as a third parameter, or by
        // configuring an `ImageProvider` to `avatarGroup`.
        UserInfo userInfo = new UserInfo(UUID.randomUUID().toString(), "Steve Lange");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        avatarGroup = new CollaborationAvatarGroup(userInfo, null);
        avatarGroup.getStyle().set("visibility", "hidden");

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("loginName").setAutoWidth(true);
        grid.addColumn("fullName").setAutoWidth(true);
        grid.addColumn("permission").setAutoWidth(true);
        grid.setItems(query -> userPermissionService.list(VaadinSpringDataHelpers.toSpringPageRequest(query)).stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(USERPERMISSION_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(EditPermissionsView.class);
            }
        });

        // Configure Form
        binder = new CollaborationBinder<>(UserPermission.class, userInfo);

        // Bind fields. This is where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.userPermission == null) {
                    this.userPermission = new UserPermission();
                }
                binder.writeBean(this.userPermission);
                userPermissionService.save(this.userPermission);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(EditPermissionsView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error updating the data. Somebody else has updated the record while you were making changes.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException validationException) {
                Notification.show("Failed to update the data. Check again that all values are valid");
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> userPermissionId = event.getRouteParameters().get(USERPERMISSION_ID).map(Long::parseLong);
        if (userPermissionId.isPresent()) {
            Optional<UserPermission> userPermissionFromBackend = userPermissionService.get(userPermissionId.get());
            if (userPermissionFromBackend.isPresent()) {
                populateForm(userPermissionFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested userPermission was not found, ID = %d", userPermissionId.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(EditPermissionsView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        loginName = new TextField("Login Name");
        fullName = new TextField("Full Name");
        permission = new TextField("Permission");
        formLayout.add(loginName, fullName, permission);

        editorDiv.add(avatarGroup, formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(UserPermission value) {
        this.userPermission = value;
        String topic = null;
        if (this.userPermission != null && this.userPermission.getId() != null) {
            topic = "userPermission/" + this.userPermission.getId();
            avatarGroup.getStyle().set("visibility", "visible");
        } else {
            avatarGroup.getStyle().set("visibility", "hidden");
        }
        binder.setTopic(topic, () -> this.userPermission);
        avatarGroup.setTopic(topic);

    }
}
