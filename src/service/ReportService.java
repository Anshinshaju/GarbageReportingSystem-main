package service;

import model.Report;
import repository.ReportRepository;
import java.util.List;

public class ReportService {
    private ReportRepository reportRepository;
    
    public ReportService() {
        this.reportRepository = new ReportRepository();
    }
    
    public boolean addReport(Report report) {
        return reportRepository.addReport(report);
    }
    
    public List<Report> getUserReports(String userId) {
        return reportRepository.getReportsByUserId(userId);
    }
    
    public List<Report> getAllReports() {
        return reportRepository.getAllReports();
    }
    
    public boolean deleteReport(int reportId) {
        return reportRepository.deleteReport(reportId);
    }
    
    public boolean updateReportStatus(int reportId, String status) {
        return reportRepository.updateReportStatus(reportId, status);
    }
}