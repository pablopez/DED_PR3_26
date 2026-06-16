package uoc.ds.pr.repository;

public class Repositories {

    private final WorkerRepository workerRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final IssueTypeRepository issueTypeRepository;
    private final AssistanceRepository assistanceRepository;
    private final RatingRepository ratingRepository;
    private final WorkerSocialNetworkRepository workerSocialNetworkRepository;

    private final SystemRepository systemRepository;
    private final ComponentRepository componentRepository;
    private final IssueRepository issueRepository;

    public Repositories() {
        this.workerRepository = new WorkerRepository();
        this.userRepository = new UserRepository();
        this.roomRepository = new RoomRepository();
        this.issueTypeRepository = new IssueTypeRepository();
        this.assistanceRepository = new AssistanceRepository();
        this.ratingRepository = new RatingRepository();
        this.workerSocialNetworkRepository = new WorkerSocialNetworkRepository();

        this.systemRepository = new SystemRepository();
        this.componentRepository = new ComponentRepository();
        this.issueRepository = new IssueRepository();
    }

    public WorkerRepository workers() {
        return workerRepository;
    }

    public UserRepository users() {
        return userRepository;
    }

    public RoomRepository rooms() {
        return roomRepository;
    }

    public IssueTypeRepository issueTypes() {
        return issueTypeRepository;
    }

    public AssistanceRepository assistances() {
        return assistanceRepository;
    }

    public RatingRepository ratings() {
        return ratingRepository;
    }

    public WorkerSocialNetworkRepository socialNetwork() {
        return workerSocialNetworkRepository;
    }

    public SystemRepository systems() {
        return systemRepository;
    }

    public ComponentRepository components() {
        return componentRepository;
    }

    public IssueRepository issues() {
        return issueRepository;
    }
}
