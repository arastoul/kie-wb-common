/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
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
package org.kie.workbench.common.screens.library.api;

import java.util.ArrayList;
import java.util.List;

import org.guvnor.common.services.project.model.Project;
import org.jboss.errai.common.client.api.annotations.MapsTo;
import org.jboss.errai.common.client.api.annotations.Portable;

import static org.kie.soup.commons.validation.PortablePreconditions.checkNotNull;

@Portable
public class LibraryInfo {

    private String selectedBranch;
    private List<Project> projects = new ArrayList<>();

    public LibraryInfo(@MapsTo("selectedBranch") final String selectedBranch,
                       @MapsTo("projects") final List<Project> projects) {
        this.selectedBranch = checkNotNull("selectedBranch",
                                           selectedBranch);
        this.projects = checkNotNull("projects",
                                     projects);
    }

    public String getSelectedBranch() {
        return selectedBranch;
    }

    public List<Project> getProjects() {
        return projects;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LibraryInfo)) {
            return false;
        }

        final LibraryInfo that = (LibraryInfo) o;

        if (getSelectedBranch() != null ? !getSelectedBranch().equals(that.getSelectedBranch()) : that.getSelectedBranch() != null) {
            return false;
        }
        return !(getProjects() != null ? !getProjects().equals(that.getProjects()) : that.getProjects() != null);
    }

    @Override
    public int hashCode() {
        int result = getSelectedBranch() != null ? getSelectedBranch().hashCode() : 0;
        result = ~~result;
        result = 31 * result + (getProjects() != null ? getProjects().hashCode() : 0);
        result = ~~result;
        return result;
    }
}
