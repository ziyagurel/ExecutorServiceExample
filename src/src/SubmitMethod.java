package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class SubmitMethod {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        List<CaseDto> itemList = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        Future<List<CaseDto>> future = executorService.submit(new Callable<List<CaseDto>>() {
            @Override
            public List<CaseDto> call() {
                return processAddedObjectList(itemList);
            }
        });

        for(int i=0; i< future.get().size(); i++)
            System.out.print(future.get().get(i).toString() + " ");
    }

    private static List<CaseDto> processAddedObjectList(List<CaseDto> itemList) {
        List<String> nameList = Arrays.asList("Zahit","Ziya","GUREL");
        List<CaseDto> dtoList = new ArrayList<>();
        for (String item : nameList){
            CaseDto caseDto = new CaseDto();
            caseDto.setName(item + " ");
            dtoList.add(caseDto);
        }
        return dtoList;
    }
}
