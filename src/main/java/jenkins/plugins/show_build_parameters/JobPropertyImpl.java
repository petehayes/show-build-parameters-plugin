package jenkins.plugins.show_build_parameters;

import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.Descriptor;
import hudson.model.Job;
import hudson.model.JobProperty;
import hudson.model.JobPropertyDescriptor;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.Ancestor;
import org.kohsuke.stapler.StaplerRequest;

import java.io.IOException;
import java.util.List;

/**
 * Job Property that enables a build action to show parameters, if any, on the
 * build
 *
 * @author Peter Hayes
 */
public final class JobPropertyImpl extends JobProperty<AbstractProject<?,?>> {

    /**
     * Programmatic construction.
     */
    public JobPropertyImpl(AbstractProject<?,?> owner) throws Descriptor.FormException, IOException {
        this.owner = owner;
    }

    private JobPropertyImpl(StaplerRequest req, JSONObject json) throws Descriptor.FormException, IOException {
        // a hack to get the owning AbstractProject.
        // this is needed here so that we can load items
        List<Ancestor> ancs = req.getAncestors();
        owner = (AbstractProject)ancs.get(ancs.size()-1).getObject();
    }

    /**
     * Gets {@link AbstractProject} that contains us.
     */
    public AbstractProject<?,?> getOwner() {
        return owner;
    }

    @Override
    public boolean prebuild(AbstractBuild<?,?> build, BuildListener listener) {
        build.addAction(new ShowParametersBuildAction(build));
        return true;
    }


    @Extension
    public static final class DescriptorImpl extends JobPropertyDescriptor {
        public String getDisplayName() {
            return Messages.JobProperty_DisplayName();
        }

        @Override
        public boolean isApplicable(Class<? extends Job> jobType) {
            return AbstractProject.class.isAssignableFrom(jobType);
        }

        @Override
        public JobPropertyImpl newInstance(StaplerRequest req, JSONObject json) throws Descriptor.FormException {
            try {
                return new JobPropertyImpl(req, json);
            } catch (IOException e) {
                throw new FormException("Failed to create",e,null);
            }
        }
    }
}
