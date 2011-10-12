package com.staticfinal.jenkins.plugins.plainoutput;

import hudson.Plugin;
import hudson.model.Result;
import hudson.model.AbstractProject;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import jenkins.model.Jenkins;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

/**
 * <p>
 * Plugin for small client like Arduino that can NOT purse XML/HTML.
 * </p>
 * @plugin plainoutput
 */
public class PluginImpl extends Plugin {

    public final static String PLUGIN_NAME = "plainoutput";

    @Override
    public void start() throws Exception {
    	System.out.println("PluginImpl start() " + this.getClass().getName());
        super.start();
    	System.out.println("PluginImpl start()~");
    }

    /**
     * <p>
     * response plain text output against URI "/plugin/plain-output-plugin/simplesummary"
     * </p>
     * @param req
     * @param res
     * @throws IOException
     */
    public void doSimplesummary(StaplerRequest req, StaplerResponse res) throws IOException {
    	System.out.println("doSimplesummary invoked...");

        String viewName = req.getOriginalRestOfPath();
    	System.out.println("viewName = " + viewName);
        viewName = StringUtils.removeStart(viewName, "/");

        /*
         * response
         * "fail" : if at least one failure
         * "success" : all build succeeded
         */
    	int failCt = 0;
    	List<AbstractProject> projects = Jenkins.getInstance().getItems(AbstractProject.class);
    	for (AbstractProject p : projects) {
    		if(Result.FAILURE.equals(p.getLastBuild().getResult()))
    			failCt++;
    	}
    	
    	if (failCt > 0) {
            res.setHeader("Content-Disposition", "filename=fail.txt");
    	} else { 
            res.setHeader("Content-Disposition", "filename=success.txt");
    	}
        res.setContentType("plain/text");
        Writer w = res.getWriter();
    	if (failCt > 0) {
    		w.write("fail," + failCt);
    	} else { 
    		w.write("success");
    	}
    	System.out.println("doSimplesummary~");
    }
}
