package uoc.ds.pr.service;

import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.*;
import uoc.ds.pr.repository.*;

import java.time.LocalDate;

public class AssistanceService {

    private final AssistanceRepository assistanceRepository;
    private final UserRepository userRepository;
    private final IssueTypeRepository issueTypeRepository;
    private final RoomRepository roomRepository;
    private final WorkerRepository workerRepository;
    private final RatingRepository ratingRepository;

    public AssistanceService(
            AssistanceRepository assistanceRepository,
            UserRepository userRepository,
            IssueTypeRepository issueTypeRepository,
            RoomRepository roomRepository,
            WorkerRepository workerRepository,
            RatingRepository ratingRepository
    ) {
        this.assistanceRepository = assistanceRepository;
        this.userRepository = userRepository;
        this.issueTypeRepository = issueTypeRepository;
        this.roomRepository = roomRepository;
        this.workerRepository = workerRepository;
        this.ratingRepository = ratingRepository;
    }

    public void addAssistance(
            String assistanceId,
            String userId,
            String issueTypeId,
            String roomId,
            LocalDate date,
            String description
    ) throws UserNotFoundException {

        User user = userRepository.getUserOrThrow(userId);
        IssueType issueType = issueTypeRepository.getIssueType(issueTypeId);
        Room room = roomRepository.getRoom(roomId);
        Assistance assistance = assistanceRepository.addAssistance(assistanceId, user, issueType, room, date, description);
        userRepository.addAssistance(user, assistance);
    }

    public void assignAssistance(String assistanceId)
            throws AssistanceNotFoundException, NoWorkerException {

        assistanceRepository.assignAssistance(assistanceId);

    }

    public Assistance solveAssistance(String workerId)
            throws WorkerNotFoundException, NoAssistanceException {

        Worker worker = workerRepository.getWorkerOrThrow(workerId);

        Assistance assistance = worker.solveNextAssistance();

        if (assistance == null) {
            throw new NoAssistanceException();
        }

        return assistance;
    }

    public void rateAssistance(
            String ratingId,
            String userId,
            String assistanceId,
            LocalDate date,
            int resolutionScore,
            int speedScore,
            int treatmentScore
    ) throws AssistanceNotResolvedException, UserIsNotCreatorException {

        Assistance assistance =
                assistanceRepository.getAssistance(assistanceId);

        if (assistance == null || !assistance.isResolved()) {
            throw new AssistanceNotResolvedException();
        }

        if (!assistance.getUser().getId().equals(userId)) {
            throw new UserIsNotCreatorException();
        }

        Rating rating = ratingRepository.addRating(
                ratingId,
                assistanceId,
                date,
                resolutionScore,
                speedScore,
                treatmentScore
        );

        assistance.setRating(rating);
        workerRepository.rateWorker(assistance.getWorker(), rating);
    }
}