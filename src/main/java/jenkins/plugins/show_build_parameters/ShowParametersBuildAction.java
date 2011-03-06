package jenkins.plugins.show_build_parameters;

import hudson.model.AbstractBuild;
import hudson.model.InvisibleAction;
import hudson.model.ParameterValue;
import hudson.model.ParametersAction;
import java.util.Collections;
import java.util.List;

/**
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
}
