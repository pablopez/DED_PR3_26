package uoc.ds.pr.service;

import uoc.ds.pr.repository.Repositories;

public class Services {

    private final CoreService coreService;
    private final InfrastructureService infrastructureService;
    private final IssueService issueService;
    private final WorkerIssueTypeService workerIssueTypeService;
    private final AssistanceService assistanceService;
    private final RankingService rankingService;
    private final WorkerSocialNetworkService workerSocialNetworkService;

    public Services(Repositories repositories) {
        this.coreService = new CoreService(
                repositories.workers(),
                repositories.users(),
                repositories.rooms(),
                repositories.issueTypes(),
                repositories.systems(),
                repositories.components(),
                repositories.issues(),
                repositories.assistances(),
                repositories.socialNetwork()
        );

        this.infrastructureService = new InfrastructureService(
                repositories.systems(),
                repositories.components(),
                repositories.rooms()
        );

        this.issueService = new IssueService(
                repositories.issues(),
                repositories.workers()
        );

        this.workerIssueTypeService = new WorkerIssueTypeService(
                repositories.workers(),
                repositories.issueTypes()
        );

        this.assistanceService = new AssistanceService(
                repositories.assistances(),
                repositories.users(),
                repositories.issueTypes(),
                repositories.rooms(),
                repositories.workers(),
                repositories.ratings()
        );

        this.rankingService = new RankingService(
                repositories.workers(),
                repositories.users(),
                repositories.systems()
        );

        this.workerSocialNetworkService = new WorkerSocialNetworkService(
                repositories.workers(),
                repositories.issueTypes(),
                repositories.socialNetwork()
        );
    }

    public CoreService core() {
        return coreService;
    }

    public InfrastructureService infra() {
        return infrastructureService;
    }

    public IssueService issues() {
        return issueService;
    }

    public WorkerIssueTypeService workerIssueTypes() {
        return workerIssueTypeService;
    }

    public AssistanceService assistances() {
        return assistanceService;
    }

    public RankingService rankings() {
        return rankingService;
    }

    public WorkerSocialNetworkService socialNetwork() {
        return workerSocialNetworkService;
    }
}