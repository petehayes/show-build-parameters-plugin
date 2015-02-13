package jenkins.plugins.show_build_parameters;

import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.InvisibleAction;
import hudson.model.ParameterValue;
import hudson.model.ParametersAction;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;
import java.util.Collections;
import java.util.List;

/**
 * Show the parameters on a build as an action.  This means that it will
 * only work for builds that have been run after this plugin was added.
 * 
 * @author Peter Hayes
 */
public class ShowParametersBuildAction extends InvisibleAction {
    private AbstractBuild<?,?> build;

    public ShowParametersBuildAction(AbstractBuild<?,?> build) {
        this.build = build;
    }

    public List<ParameterValue> getParameters() {
        ParametersAction parameters = build.getAction(ParametersAction.class);

        if (parameters != null) {
            return parameters.getParameters();
        }

        return Collections.EMPTY_LIST;
    }

    @Extension
    public static final class RunListenerImpl extends RunListener<AbstractBuild<?,?>> {

        @Override
        public void onStarted(AbstractBuild<?, ?> r, TaskListener listener) {
            if (r.getAction(ParametersAction.class) != null)
                r.addAction(new ShowParametersBuildAction(r));
        }
    }
}
