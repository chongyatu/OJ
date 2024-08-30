package one.sunny.ttoj.pojo.params.oj;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import one.sunny.commonutils.ErrorCode;
import one.sunny.ttoj.exception.TTOJException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JudgeParams {
    private String src;
    private Integer max_cpu_time;
    private Integer max_memory;
    private String test_case_id;
    private LanguageConfig language_config;
    private Boolean output;

    public JudgeParams(String language,
                       String src,
                       Integer max_cpu_time,
                       Integer max_memory,
                       String test_case_id,
                       Boolean output){
        this.setSrc(src);
        this.setMax_cpu_time(max_cpu_time);
        this.setMax_memory(max_memory);
        this.setTest_case_id(test_case_id);
        this.setOutput(output);
        this.setLanguage_config(new LanguageConfig(language));
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class LanguageConfig {
        private String template;
        private Compile compile;
        private Run run;

        public LanguageConfig(String language){
            this.setCompile(new Compile(language));
            this.setRun(new Run(language));
            this.setTemplate("");
        }
        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        class Compile {
            public String src_name;
            public String exe_name;
            public Long max_cpu_time;
            public Long max_real_time;
            public Long max_memory;
            private List<String> env;
            public String compile_command;

            public Compile(String type) {
                String srcName, exeName, compileCmd;
                long maxCpuTime, maxRealTime, maxMemory;
                if ("cpp".equals(type)) {
                    srcName = "main.cpp";
                    exeName = "main";
                    maxCpuTime = 10000;
                    maxRealTime = 20000;
                    maxMemory = 1073741824;
                    compileCmd = "/usr/bin/g++ -DONLINE_JUDGE -O2 -w -fmax-errors=3 -std=c++14 {src_path} -lm -o {exe_path}";
                    this.setEnv(new ArrayList<>());
                }else if("java".equals(type)){
                    srcName = "Main.java";
                    exeName = "Main";
                    maxCpuTime = 5000;
                    maxRealTime = 10000;
                    maxMemory = -1;
                    compileCmd = "/usr/bin/javac {src_path} -d {exe_dir} -encoding UTF8";
                    this.setEnv(new ArrayList<>());
                }else if("c".equals(type)){
                    srcName = "main.c";
                    exeName = "main";
                    maxCpuTime = 3000;
                    maxRealTime = 10000;
                    maxMemory = 268435456;
                    compileCmd = "/usr/bin/gcc -DONLINE_JUDGE -O2 -w -fmax-errors=3 -std=c11 {src_path} -lm -o {exe_path}";
                    this.setEnv(new ArrayList<>());
                }else if("python2".equals(type)){
                    srcName = "solution.py";
                    exeName = "solution.pyc";
                    maxCpuTime = 3000;
                    maxRealTime = 10000;
                    maxMemory = 268435456;
                    compileCmd = "/usr/bin/python -m py_compile {src_path}";
                    this.setEnv(new ArrayList<>());
                }else if("python3".equals(type)){
                    srcName = "solution.py";
                    exeName = "__pycache__/solution.cpython-36.pyc";
                    maxCpuTime = 3000;
                    maxRealTime = 10000;
                    maxMemory = 268435456;
                    compileCmd = "/usr/bin/python3 -m py_compile {src_path}";
                    this.setEnv(new ArrayList<>());
                }else if("javascript".equals(type)){
                    srcName = "main.js";
                    exeName = "main.js";
                    maxCpuTime = 3000;
                    maxRealTime = 5000;
                    maxMemory = 1073741824;
                    compileCmd = "/usr/bin/node --check {src_path}";
                    List<String> env = new ArrayList<>();
                    env.add("LANG=en_US.UTF-8");
                    env.add("LANGUAGE=en_US:en");
                    env.add("LC_ALL=en_US.UTF-8");
                    this.setEnv(env);
                }else if("go".equals(type)){
                    srcName = "main.go";
                    exeName = "main";
                    maxCpuTime = 3000;
                    maxRealTime = 5000;
                    maxMemory = 1073741824;
                    compileCmd = "/usr/bin/go build -o {exe_path} {src_path}";
                    List<String> env = new ArrayList<>();
                    env.add("GOCACHE=/tmp");
                    env.add("GOPATH=/tmp");
                    env.add("GOMAXPROCS=1");
                    env.add("LANG=en_US.UTF-8");
                    env.add("LANGUAGE=en_US:en");
                    env.add("LC_ALL=en_US.UTF-8");
                    this.setEnv(env);
                } else{
                    throw new TTOJException(ErrorCode.PARAM_ERROR.getCode(), "语言不存在");
                }
                this.setSrc_name(srcName);
                this.setExe_name(exeName);
                this.setMax_cpu_time(maxCpuTime);
                this.setMax_real_time(maxRealTime);
                this.setMax_memory(maxMemory);
                this.setCompile_command(compileCmd);
            }
        }
        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        class Run {
            private String command;
            private Object seccomp_rule;
            private List<String> env;
            private Integer memory_limit_check_only;

            public Run(String type){
                if("cpp".equals(type)){
                    this.setCommand("{exe_path}");
                    HashMap<String, String> map = new HashMap<>();
                    map.put("Standard IO", "c_cpp");
                    map.put("File IO", "c_cpp_file_io");
                    this.setSeccomp_rule(map);
                    List<String> env = new ArrayList<>();
                    env.add("LANG=en_US.UTF-8");
                    env.add("LANGUAGE=en_US:en");
                    env.add("LC_ALL=en_US.UTF-8");
                    this.setEnv(env);
                    this.setMemory_limit_check_only(0);
                }else if("java".equals(type)){
                    this.setCommand("/usr/bin/java -cp {exe_dir} -XX:MaxRAM={max_memory}k -Djava.security.manager -Dfile.encoding=UTF-8 -Djava.security.policy==/etc/java_policy -Djava.awt.headless=true Main");
                    this.setSeccomp_rule(null);
                    List<String> env = new ArrayList<>();
                    env.add("LANG=en_US.UTF-8");
                    env.add("LANGUAGE=en_US:en");
                    env.add("LC_ALL=en_US.UTF-8");
                    this.setEnv(env);
                    this.setMemory_limit_check_only(1);
                }else if("c".equals(type)){
                    this.setCommand("{exe_path}");
                    HashMap<String, String> map = new HashMap<>();
                    map.put("Standard IO", "c_cpp");
                    map.put("File IO", "c_cpp_file_io");
                    this.setSeccomp_rule(map);
                    List<String> env = new ArrayList<>();
                    env.add("LANG=en_US.UTF-8");
                    env.add("LANGUAGE=en_US:en");
                    env.add("LC_ALL=en_US.UTF-8");
                    this.setEnv(env);
                    this.setMemory_limit_check_only(0);
                }else if("python2".equals(type)){
                    this.setCommand("/usr/bin/python {exe_path}");
                    this.setSeccomp_rule("general");
                    List<String> env = new ArrayList<>();
                    env.add("LANG=en_US.UTF-8");
                    env.add("LANGUAGE=en_US:en");
                    env.add("LC_ALL=en_US.UTF-8");
                    this.setEnv(env);
                    this.setMemory_limit_check_only(0);
                }else if("python3".equals(type)){
                    this.setCommand("/usr/bin/python3 {exe_path}");
                    this.setSeccomp_rule("general");
                    List<String> env = new ArrayList<>();
                    env.add("LANG=en_US.UTF-8");
                    env.add("LANGUAGE=en_US:en");
                    env.add("LC_ALL=en_US.UTF-8");
                    env.add("PYTHONIOENCODING=utf-8");
                    this.setEnv(env);
                    this.setMemory_limit_check_only(0);
                }else if("javascript".equals(type)){
                    this.setCommand("/usr/bin/node {exe_path}");
                    this.setSeccomp_rule("node");
                    List<String> env = new ArrayList<>();
                    env.add("LANG=en_US.UTF-8");
                    env.add("LANGUAGE=en_US:en");
                    env.add("LC_ALL=en_US.UTF-8");
                    this.setEnv(env);
                    this.setMemory_limit_check_only(1);
                } else if("go".equals(type)){
                    this.setCommand("{exe_path}");
                    this.setSeccomp_rule("golang");
                    List<String> env = new ArrayList<>();
                    env.add("GODEBUG=madvdontneed=1");
                    env.add("GOMAXPROCS=1");
                    env.add("LANG=en_US.UTF-8");
                    env.add("LANGUAGE=en_US:en");
                    env.add("LC_ALL=en_US.UTF-8");
                    this.setEnv(env);
                    this.setMemory_limit_check_only(1);
                } else{
                    throw new TTOJException(ErrorCode.PARAM_ERROR.getCode(), "语言不存在");
                }
            }
        }
    }
}
