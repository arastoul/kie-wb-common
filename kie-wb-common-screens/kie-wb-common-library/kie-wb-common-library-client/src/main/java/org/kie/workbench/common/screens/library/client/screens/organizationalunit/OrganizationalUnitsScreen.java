/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.workbench.common.screens.library.client.screens.organizationalunit;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.guvnor.common.services.project.context.ProjectContextChangeEvent;
import org.guvnor.structure.client.security.OrganizationalUnitController;
import org.guvnor.structure.events.AfterCreateOrganizationalUnitEvent;
import org.guvnor.structure.organizationalunit.OrganizationalUnit;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.dom.HTMLElement;
import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.kie.workbench.common.screens.library.api.LibraryService;
import org.kie.workbench.common.screens.library.api.OrganizationalUnitRepositoryInfo;
import org.kie.workbench.common.screens.library.api.preferences.LibraryInternalPreferences;
import org.kie.workbench.common.screens.library.client.perspective.LibraryPerspective;
import org.kie.workbench.common.screens.library.client.screens.organizationalunit.popup.OrganizationalUnitPopUpPresenter;
import org.kie.workbench.common.screens.library.client.util.LibraryPlaces;
import org.kie.workbench.common.screens.library.client.widgets.common.TileWidget;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.client.mvp.UberElement;

@WorkbenchScreen(identifier = LibraryPlaces.ORGANIZATIONAL_UNITS_SCREEN,
        owningPerspective = LibraryPerspective.class)
public class OrganizationalUnitsScreen {

    public interface View extends UberElement<OrganizationalUnitsScreen> {

        void clearOrganizationalUnits();

        void hideCreateOrganizationalUnitAction();

        void addOrganizationalUnit(TileWidget tileWidget);

        String getNumberOfContributorsLabel(int numberOfContributors);

        String getNumberOfRepositoriesLabel(int numberOfRepositories);

        void showNoOrganizationalUnits(HTMLElement view);
    }

    private View view;

    private LibraryPlaces libraryPlaces;

    private Caller<LibraryService> libraryService;

    private OrganizationalUnitPopUpPresenter organizationalUnitPopUpPresenter;

    private OrganizationalUnitController organizationalUnitController;

    private ManagedInstance<TileWidget> organizationalUnitTileWidgets;

    private Event<ProjectContextChangeEvent> projectContextChangeEvent;

    private LibraryInternalPreferences libraryInternalPreferences;

    private EmptyOrganizationalUnitsScreen emptyOrganizationalUnitsScreen;

    List<OrganizationalUnit> organizationalUnits;

    @Inject
    public OrganizationalUnitsScreen(final View view,
                                     final LibraryPlaces libraryPlaces,
                                     final Caller<LibraryService> libraryService,
                                     final OrganizationalUnitPopUpPresenter organizationalUnitPopUpPresenter,
                                     final OrganizationalUnitController organizationalUnitController,
                                     final ManagedInstance<TileWidget> organizationalUnitTileWidgets,
                                     final Event<ProjectContextChangeEvent> projectContextChangeEvent,
                                     final LibraryInternalPreferences libraryInternalPreferences,
                                     final EmptyOrganizationalUnitsScreen emptyOrganizationalUnitsScreen) {
        this.view = view;
        this.libraryPlaces = libraryPlaces;
        this.libraryService = libraryService;
        this.organizationalUnitPopUpPresenter = organizationalUnitPopUpPresenter;
        this.organizationalUnitController = organizationalUnitController;
        this.organizationalUnitTileWidgets = organizationalUnitTileWidgets;
        this.projectContextChangeEvent = projectContextChangeEvent;
        this.libraryInternalPreferences = libraryInternalPreferences;
        this.emptyOrganizationalUnitsScreen = emptyOrganizationalUnitsScreen;
    }

    @PostConstruct
    public void init() {
        setupView();
        setupOrganizationalUnits();
    }

    private void setupView() {
        if (!canCreateOrganizationalUnit()) {
            view.hideCreateOrganizationalUnitAction();
        }
    }

    private void setupOrganizationalUnits() {
        if (organizationalUnitController.canReadOrgUnits()) {
            libraryService.call((List<OrganizationalUnit> allOrganizationalUnits) -> {
                organizationalUnits = allOrganizationalUnits;
                if (allOrganizationalUnits.isEmpty()) {
                    view.showNoOrganizationalUnits(emptyOrganizationalUnitsScreen.getView().getElement());
                } else {
                    refresh();
                }
            }).getOrganizationalUnits();
        }
    }

    public void refresh() {
        view.clearOrganizationalUnits();
        organizationalUnits.forEach(organizationalUnit -> {
            final TileWidget tileWidget = organizationalUnitTileWidgets.get();
            tileWidget.init(organizationalUnit.getName(),
                            view.getNumberOfContributorsLabel(organizationalUnit.getContributors().size()),
                            String.valueOf(organizationalUnit.getRepositories().size()),
                            view.getNumberOfRepositoriesLabel(organizationalUnit.getRepositories().size()),
                            () -> open(organizationalUnit));
            view.addOrganizationalUnit(tileWidget);
        });
    }

    public OrganizationalUnitRepositoryInfo open(OrganizationalUnit organizationalUnit) {
        return libraryService.call((OrganizationalUnitRepositoryInfo info) -> {
            libraryInternalPreferences.load(loadedLibraryInternalPreferences -> {
                                                loadedLibraryInternalPreferences.setLastOpenedOrganizationalUnit(info.getSelectedOrganizationalUnit().getIdentifier());
                                                loadedLibraryInternalPreferences.setLastOpenedRepository(info.getSelectedRepository().getAlias());
                                                loadedLibraryInternalPreferences.save();
                                            },
                                            error -> {
                                            });

            if (teamAlreadySelected(info)) {
                libraryPlaces.goToLibrary(() -> {
                });
            } else {
                final ProjectContextChangeEvent event = new ProjectContextChangeEvent(info.getSelectedOrganizationalUnit(),
                                                                                      info.getSelectedRepository(),
                                                                                      info.getSelectedRepository().getDefaultBranch());
                projectContextChangeEvent.fire(event);
            }
        }).getOrganizationalUnitRepositoryInfo(organizationalUnit);
    }

    private boolean teamAlreadySelected(OrganizationalUnitRepositoryInfo info) {
        return info.getSelectedOrganizationalUnit().equals(libraryPlaces.getSelectedOrganizationalUnit())
                && info.getSelectedRepository().equals(libraryPlaces.getSelectedRepository())
                && info.getSelectedRepository().getDefaultBranch().equals(libraryPlaces.getSelectedBranch());
    }

    public void createOrganizationalUnit() {
        organizationalUnitPopUpPresenter.show();
    }

    public void organizationalUnitCreated(@Observes final AfterCreateOrganizationalUnitEvent afterCreateOrganizationalUnitEvent) {
        organizationalUnits.add(afterCreateOrganizationalUnitEvent.getOrganizationalUnit());
        refresh();
    }

    public boolean canCreateOrganizationalUnit() {
        return organizationalUnitController.canCreateOrgUnits();
    }

    @WorkbenchPartTitle
    public String getTitle() {
        return "Organizational Units Screen";
    }

    @WorkbenchPartView
    public UberElement<OrganizationalUnitsScreen> getView() {
        return view;
    }
}
