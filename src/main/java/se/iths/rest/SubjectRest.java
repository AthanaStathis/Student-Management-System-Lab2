package se.iths.rest;


import se.iths.entity.Student;
import se.iths.entity.Subject;
import se.iths.exception.CustomBadRequestException;
import se.iths.service.StudentService;
import se.iths.service.SubjectService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("subjects")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SubjectRest {

    SubjectService subjectService;
    StudentService studentService;


    @Inject
    public SubjectRest(SubjectService subjectService, StudentService studentService) {
       this.subjectService = subjectService;
       this.studentService = studentService;
    }

//    @Inject
//    SubjectService subjectService;

    @Path("")
    @POST
    public Response createSubject(Subject subject) {
        subjectService.createSubject(subject);
        return Response.ok(subject).build();
    }

    @Path("")
    @PUT
    public Response updateSubject(Subject subject) {
        subjectService.updateSubject(subject);
        return Response.ok(subject).build();
    }

    @Path("{id}")
    @GET
    public Response getSubject(@PathParam("id") Long id) {
        Subject foundSubject = subjectService.findSubjectById(id);
        if (foundSubject == null) {
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
                    .entity("Subject with ID " + id + " was not found in database.").type(MediaType.TEXT_PLAIN_TYPE).build());
        }
        return Response.ok(foundSubject).build();
    }

    @Path("")
    @GET
    public Response getAllSubjects() {
        List<Subject> foundSubjects = subjectService.getAllSubjects();
        return Response.ok(foundSubjects).build();
    }

    @Path("{id}")
    @DELETE
    public Response deleteSubject(@PathParam("id") Long id) {
        subjectService.deleteSubject(id);
        return Response.ok().build();
    }

    @Path("updatename/{id}")
    @PATCH
    public Response updateName(@PathParam("id") Long id, @QueryParam("name") String name) {
        Subject updatedSubject = subjectService.updateName(id, name);
        return Response.ok(updatedSubject).build();
    }

    @Path("subject/{subjectId}/student/{studentId}")
    @PATCH
    public Response addStudentToSubject(@PathParam("subjectId") Long subjectId, @PathParam("studentId") Long studentId) {
        Subject subjectOfInterest = subjectService.findSubjectById(subjectId);
        Student studentToBeAdded = studentService.findStudentById(studentId);
        if (subjectOfInterest == null || studentToBeAdded == null)
            throw new CustomBadRequestException();
        subjectOfInterest.addStudent(studentToBeAdded);
        return Response.ok(subjectOfInterest).build();
    }


}
