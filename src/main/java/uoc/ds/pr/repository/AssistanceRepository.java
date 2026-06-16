package uoc.ds.pr.repository;

import uoc.ds.pr.exceptions.AssistanceNotFoundException;
import uoc.ds.pr.exceptions.NoWorkerException;
import uoc.ds.pr.model.Assistance;
import uoc.ds.pr.model.IssueType;
import uoc.ds.pr.model.Room;
import uoc.ds.pr.model.User;
import uoc.ds.pr.model.Worker;
import uoc.ds.pr.storage.AVLStorageStrategy;

import java.time.LocalDate;

public class AssistanceRepository extends AbstractRepository<Assistance> {

    public AssistanceRepository() {
        super(new AVLStorageStrategy<>());
    }

    public Assistance addAssistance(
            String id,
            User user,
            IssueType issueType,
            Room room,
            LocalDate date,
            String description
    ) {
        Assistance assistance = getById(id);

        if (assistance == null) {
            assistance = new Assistance(
                    id,
                    user,
                    issueType,
                    room,
                    date,
                    description
            );

            save(assistance);
        }/*else{

               habría que devolver un error en caso de que se intentara duplicar una asistencia? o editarla?
               dejo este bloque pendiente para ver qué hacer y devolviendo la asistencia tal cual

        }*/
        return assistance;
    }

    public Assistance getAssistance(String id) {
        return getById(id);
    }

    public Assistance getAssistanceOrThrow(String id)
            throws AssistanceNotFoundException {
        return getByIdOrThrow(id, AssistanceNotFoundException::new);
    }

    public int numAssistances() {
        return size();
    }

    public void assignAssistance(String id) throws AssistanceNotFoundException, NoWorkerException {
        Assistance assistance = getAssistanceOrThrow(id);
        IssueType issueType = assistance.getIssueType();

        if (issueType == null) {
            throw new NoWorkerException();
        }

        Worker worker = issueType.nextWorkerRoundRobin();

        if (worker == null) {
            throw new NoWorkerException();
        }

        assistance.setWorker(worker);
        worker.addAssignedAssistance(assistance);
    }}
