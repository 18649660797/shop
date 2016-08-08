package top.gabin.shop.core.jpa.criteria.query.dto;

import java.util.List;
import java.util.Map;

public class PageDTO<T> {
    private int page = 1;
    private int pageSize = 20;
    private long totalSize;
    private List<T> content;
    private Map<String, List<T>> contentMap;
    private int totalCountByOne = 0;

    public int getPageSize() {
        return pageSize;
    }


    public PageDTO(int page, int pageSize, int totalSize, List<T> content){
        this.page = page;
        this.pageSize = pageSize;
        this.totalSize = totalSize;
        this.content = content;
    }

    public PageDTO(int pageIndex, int pageSize, long totalSize, List<T> content){
        this.page = pageIndex;
        this.pageSize = pageSize;
        this.totalSize = totalSize;
        this.content = content;
    }

    public PageDTO(int pageIndex, int pageSize, int totalSize, List<T> content, Map<String, List<T>> contentMap){
        this.page = pageIndex;
        this.pageSize = pageSize;
        this.totalSize = totalSize;
        this.content = content;
        this.contentMap = contentMap;
    }


    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalPage() {
        if(pageSize!=0){
            long result = totalSize/pageSize;
            if(totalSize % pageSize !=0){
                result ++;
            }
            return result;
        }
        return 0;
    }


    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public Map<String, List<T>> getContentMap() {
        return contentMap;
    }

    public void setContentMap(Map<String, List<T>> contentMap) {
        this.contentMap = contentMap;
    }

    public boolean hasPreviousPage(){
        if(isFirstPage()){
            return false;
        }
        return true;
    }
    public boolean isFirstPage(){
         if(page<=1){
             return true;
         }
        return false;
    }

    public boolean hasNextPage(){
        if(isLastPage()){
            return false;
        }else{
            return true;
        }
    }

    public boolean isLastPage(){
        if(page>=this.getTotalPage()){
            return true;
        }else{
            return false;
        }
    }

    public int getPage() {
        if(getTotalPage() == 0){
            return 0;
        }
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalCountByOne() {
        return totalCountByOne;
    }

    public void setTotalCountByOne(int totalCountByOne) {
        this.totalCountByOne = totalCountByOne;
    }
}
