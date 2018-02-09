package kth.se.id2208.vt18.project.tutorial.services.common.model;

import java.util.List;

/**
 * @author Kim Hammar on 2018-01-13.
 */
public class HomeWork extends CourseWork {
    public HomeWork(String ID, Grading grading, List<Literature> literatures, float credits, String description) {
        super(ID, grading, literatures, credits, description);
    }
}
