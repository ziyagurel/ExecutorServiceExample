package src;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

class CustorService {

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
        List<List<CaseDto>> caseDtoListDivided = converDto("ziya", 2, 10);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Future<List<List<CaseDto>>>> futures = new ArrayList<>();
        for (final List<CaseDto> subList : caseDtoListDivided){
            Future<List<List<CaseDto>>> future = executorService.submit(new Callable<List<List<CaseDto>>>() {
                @Override
                public List<List<CaseDto>> call() {
                    return processAddedObjectList(subList);
                }
            });
            futures.add(future);
        }
        List<List<CaseDto>> caseDto = new ArrayList<>();

        for (Future<List<List<CaseDto>>> f : futures) {
            for (List<CaseDto> l : f.get()){
                caseDto.add(l);
            }
        }
        System.out.println(caseDto);
        writeToNotPad(caseDto, createUyapClosedThemisOpenCasesListHeader());
    }

    public static List<String> createUyapClosedThemisOpenCasesListHeader() {
        List<String> headers = new ArrayList<>();
        headers.add("Takip No");
        headers.add("İcra Dairesi");
        headers.add("Dosya No");
        headers.add("Müşteri Adı");
        headers.add("Uyap Dosya Durumu");
        headers.add("Uyap Türe Bağlı Dosya Durumu");
        headers.add("Hukuk Bürosu");
        return headers;
    }

    private static void writeToNotPad(List<List<CaseDto>> caseDto, List<String> uyapClosedThemisOpenCasesListHeader) throws IOException {

        OutputStreamWriter fileWriter = new OutputStreamWriter(Files.newOutputStream(Paths.get("D:\\Example.txt")), StandardCharsets.UTF_8);
        PrintWriter printWriter = new PrintWriter(fileWriter);

        for (String item : uyapClosedThemisOpenCasesListHeader){
            printWriter.printf(item + ';', 1000);
        }
        printWriter.println();
        for (List<CaseDto> caseDtos : caseDto){
            for (CaseDto dto : caseDtos){
                printWriter.printf(dto.getName() + ';', 1000);
            }
            printWriter.println();
        }
        printWriter.close();
    }

    private static List<List<CaseDto>> processAddedObjectList(List<CaseDto> subList) {
        List<List<CaseDto>> objectList = new ArrayList<>();
        for(CaseDto caseDto : subList){
            caseDto.setName(caseDto.getName() + "Gürel");
        }
        objectList.add(subList);
        return objectList;
    }

    private static List<List<CaseDto>> converDto(String item, Integer threadCount, int rowCount) {
        List<CaseDto> caseDtos = new ArrayList<>();
        for (int i=0; i<threadCount*rowCount; i++){
            CaseDto dto = new CaseDto();
            dto.setName(item);
            caseDtos.add(dto);
        }
        return piece(caseDtos, threadCount, rowCount);
    }

    public static List<List<CaseDto>> piece(List<CaseDto> caseDtoList, Integer threadCount, int rowCount) {
        List<List<CaseDto>> caseDtoListDivided = new ArrayList<>();

        threadDistributionLoop:
        for (int i = 0; i < threadCount; i++) {

            int from = i * rowCount;
            int to = (i + 1) * rowCount;

            boolean reachedEndOfList = false;

            if( to >= caseDtoList.size()){
                to = caseDtoList.size();
                reachedEndOfList = true;
            }

            caseDtoListDivided.add(caseDtoList.subList(from, to));

            if(reachedEndOfList)
                break threadDistributionLoop;

        }

        return caseDtoListDivided;
    }

    public List<List<List<Object>>> mergeListInToPieces(List<List<Object>> objectList, int threadCount, int casePerThread) {
        List<List<List<Object>>> objectListMerged = new ArrayList<>();
        int period = BigDecimal.valueOf(new BigDecimal(objectList.size()).doubleValue() / (threadCount * casePerThread)).setScale(1, RoundingMode.HALF_UP).intValue();
        for(int i=0; i< period; i++){
            int from = i * threadCount * casePerThread;
            int to = (i + 1) * threadCount * casePerThread;
            if(to >= objectList.size())
                to = objectList.size();
            objectListMerged.add(getObjectListByPiecesPeriods(objectList.subList(from,to), threadCount, casePerThread));
        }

        return objectListMerged;
    }

    private List<List<Object>> getObjectListByPiecesPeriods(List<List<Object>> objectList, int threadCount, int casePerThread) {
        List<List<Object>> objectListDived = new ArrayList<>();
        threadDistributionLoop:
        for(int i=0; i<threadCount; i++){

            int from = i * casePerThread;
            int to = (i + 1) * casePerThread;

            boolean reachedEndOfList = false;

            if (to >= objectList.size()) {
                to = objectList.size();
                reachedEndOfList = true;
            }
            objectListDived.addAll(objectList.subList(from,to));

            if (reachedEndOfList) {
                break threadDistributionLoop;
            }
        }
        return objectListDived;
    }
}