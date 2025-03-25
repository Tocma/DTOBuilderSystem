package test;

import model.EngineerBuilder;
import model.EngineerDTO;
import util.LogHandler;
import util.MessageEnum;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

/**
 * EngineerBuilder.javaの機能試験を行うテストクラス
 * コマンドライン引数を活用して様々なテストケースを実行します
 *
 * 実行方法：
 * java test.TestCoreSystem [テストケース] [パラメータ...]
 *
 * テストケース:
 * 1: 正常系 - 必須項目のみ設定
 * 2: 正常系 - すべての項目を設定
 * 3: 異常系 - 社員ID未設定
 * 4: 異常系 - 氏名未設定
 * 5: 異常系 - フリガナ未設定
 * 6: 異常系 - 生年月日未設定
 * 7: 異常系 - 入社年月未設定
 * 8: 異常系 - エンジニア歴が負の値
 * 9: 異常系 - プログラミング言語未設定
 * 10: 異常系 - 技術力の評価範囲外（0.5）
 * 11: 異常系 - 技術力の評価範囲外（5.5）
 * 12: 異常系 - 受講態度の評価範囲外
 * 13: カスタム - コマンドライン引数でEngineerDTOを構築
 *
 * @author Test Engineer
 * @version 1.0
 */
public class TestCoreSystem {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        // ログハンドラの初期化
        initializeLogHandler();

        try {
            if (args.length == 0) {
                printUsage();
                return;
            }

            int testCase = Integer.parseInt(args[0]);
            System.out.println("テストケース " + testCase + " を実行します...");

            switch (testCase) {
                case 1:
                    testRequiredFieldsOnly();
                    break;
                case 2:
                    testAllFields();
                    break;
                case 3:
                    testMissingId();
                    break;
                case 4:
                    testMissingName();
                    break;
                case 5:
                    testMissingNameKana();
                    break;
                case 6:
                    testMissingBirthDate();
                    break;
                case 7:
                    testMissingJoinDate();
                    break;
                case 8:
                    testNegativeCareer();
                    break;
                case 9:
                    testMissingProgrammingLanguages();
                    break;
                case 10:
                    testTechnicalSkillBelowRange();
                    break;
                case 11:
                    testTechnicalSkillAboveRange();
                    break;
                case 12:
                    testLearningAttitudeOutOfRange();
                    break;
                case 13:
                    if (args.length < 7) {
                        System.out.println("カスタムテストには最低6つのパラメータが必要です。");
                        printCustomUsage();
                        return;
                    }
                    testCustom(args);
                    break;
                default:
                    System.out.println("無効なテストケースです。1から13の値を入力してください。");
                    printUsage();
            }
        } catch (NumberFormatException e) {
            System.out.println("テストケースには数値を入力してください。");
            printUsage();
        } catch (Exception e) {
            System.out.println("テスト実行中にエラーが発生しました: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // ログハンドラのクリーンアップ
            LogHandler.getInstance().cleanup();
        }
    }

    /**
     * ログハンドラを初期化します
     */
    private static void initializeLogHandler() {
        try {
            LogHandler.getInstance().initialize("test_logs");
            System.out.println("ログハンドラを初期化しました。");
        } catch (IOException e) {
            System.err.println("ログハンドラの初期化に失敗しました: " + e.getMessage());
        }
    }

    /**
     * 使用方法を表示します
     */
    private static void printUsage() {
        System.out.println("\n使用方法: java test.TestCoreSystem [テストケース]");
        System.out.println("\nテストケース:");
        System.out.println("  1: 正常系 - 必須項目のみ設定");
        System.out.println("  2: 正常系 - すべての項目を設定");
        System.out.println("  3: 異常系 - 社員ID未設定");
        System.out.println("  4: 異常系 - 氏名未設定");
        System.out.println("  5: 異常系 - フリガナ未設定");
        System.out.println("  6: 異常系 - 生年月日未設定");
        System.out.println("  7: 異常系 - 入社年月未設定");
        System.out.println("  8: 異常系 - エンジニア歴が負の値");
        System.out.println("  9: 異常系 - プログラミング言語未設定");
        System.out.println("  10: 異常系 - 技術力の評価範囲外（0.5）");
        System.out.println("  11: 異常系 - 技術力の評価範囲外（5.5）");
        System.out.println("  12: 異常系 - 受講態度の評価範囲外");
        System.out.println("  13: カスタム - コマンドライン引数でEngineerDTOを構築");
        System.out.println("\nカスタムテストの使用方法については、テストケース13を指定してください。");
    }

    /**
     * カスタムテストの使用方法を表示します
     */
    private static void printCustomUsage() {
        System.out.println("\nカスタムテストの使用方法:");
        System.out.println(
                "java test.TestCoreSystem 13 [社員ID] [氏名] [フリガナ] [生年月日(yyyy-MM-dd)] [入社年月(yyyy-MM-dd)] [エンジニア歴] [言語1,言語2,...]");
        System.out.println("\n任意パラメータ (省略可):");
        System.out.println("[経歴] [研修履歴] [技術力(1.0-5.0)] [受講態度(1.0-5.0)] [コミュニケーション能力(1.0-5.0)] [リーダーシップ(1.0-5.0)] [備考]");
        System.out.println("\n例:");
        System.out.println(
                "java test.TestCoreSystem 13 ID00001 山田太郎 ヤマダタロウ 1990-01-15 2015-04-01 8 Java,Python,JavaScript \"大手SIer5年,ベンチャー3年\" \"Java研修,AWS研修\" 4.5 4.0 3.5 3.0 \"特になし\"");
    }

    /**
     * 必須項目のみ設定するテスト
     */
    private static void testRequiredFieldsOnly() {
        try {
            LogHandler.getInstance().log(MessageEnum.LOG_INFO_SYSTEM_START);
            LogHandler.getInstance().log(Level.INFO, "必須項目のみ設定するテストを開始します");

            EngineerDTO engineer = new EngineerBuilder()
                    .setId("ID00001")
                    .setName("山田太郎")
                    .setNameKana("ヤマダタロウ")
                    .setBirthDate(LocalDate.of(1990, 1, 15))
                    .setJoinDate(LocalDate.of(2020, 4, 1))
                    .setCareer(5)
                    .setProgrammingLanguages(Arrays.asList("Java", "Python"))
                    .build();

            printEngineerInfo(engineer);
            LogHandler.getInstance().log(Level.INFO, "必須項目のみ設定するテストが正常に完了しました");
        } catch (Exception e) {
            System.out.println("テスト失敗: " + e.getMessage());
            LogHandler.getInstance().logError("必須項目のみ設定するテストが失敗しました", e);
        }
    }

    /**
     * すべての項目を設定するテスト
     */
    private static void testAllFields() {
        try {
            LogHandler.getInstance().log(Level.INFO, "すべての項目を設定するテストを開始します");

            EngineerDTO engineer = new EngineerBuilder()
                    .setId("ID00002")
                    .setName("鈴木花子")
                    .setNameKana("スズキハナコ")
                    .setBirthDate(LocalDate.of(1985, 5, 20))
                    .setJoinDate(LocalDate.of(2015, 10, 1))
                    .setCareer(10)
                    .setProgrammingLanguages(Arrays.asList("Java", "Python", "JavaScript", "C#"))
                    .setCareerHistory("大手SIer7年、ベンチャー3年")
                    .setTrainingHistory("Java上級研修、AWS認定ソリューションアーキテクト、Scrum Master研修")
                    .setTechnicalSkill(4.5)
                    .setLearningAttitude(4.0)
                    .setCommunicationSkill(3.5)
                    .setLeadership(4.0)
                    .setNote("プロジェクトリーダーとしての経験が豊富")
                    .build();

            printEngineerInfo(engineer);
            LogHandler.getInstance().log(Level.INFO, "すべての項目を設定するテストが正常に完了しました");
        } catch (Exception e) {
            System.out.println("テスト失敗: " + e.getMessage());
            LogHandler.getInstance().logError("すべての項目を設定するテストが失敗しました", e);
        }
    }

    /**
     * 社員ID未設定のテスト
     */
    private static void testMissingId() {
        try {
            LogHandler.getInstance().log(Level.INFO, "社員ID未設定のテストを開始します");

            EngineerDTO engineer = new EngineerBuilder()
                    .setName("山田太郎")
                    .setNameKana("ヤマダタロウ")
                    .setBirthDate(LocalDate.of(1990, 1, 15))
                    .setJoinDate(LocalDate.of(2020, 4, 1))
                    .setCareer(5)
                    .setProgrammingLanguages(Arrays.asList("Java", "Python"))
                    .build();

            printEngineerInfo(engineer);
            System.out.println("予期せぬ成功: 社員ID未設定でもエラーが発生しませんでした");
            LogHandler.getInstance().log(Level.WARNING, "社員ID未設定のテストが予期せず成功しました");
        } catch (IllegalStateException e) {
            System.out.println("テスト成功: 期待通りのエラーが発生しました - " + e.getMessage());
            LogHandler.getInstance().log(Level.INFO, "社員ID未設定のテストが正常に完了しました: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("テスト失敗: 予期しないエラーが発生しました - " + e.getMessage());
            LogHandler.getInstance().logError("社員ID未設定のテストで予期しないエラーが発生しました", e);
        }
    }

    /**
     * 氏名未設定のテスト
     */
    private static void testMissingName() {
        try {
            LogHandler.getInstance().log(Level.INFO, "氏名未設定のテストを開始します");

            EngineerDTO engineer = new EngineerBuilder()
                    .setId("ID00003")
                    .setNameKana("ヤマダタロウ")
                    .setBirthDate(LocalDate.of(1990, 1, 15))
                    .setJoinDate(LocalDate.of(2020, 4, 1))
                    .setCareer(5)
                    .setProgrammingLanguages(Arrays.asList("Java", "Python"))
                    .build();

            printEngineerInfo(engineer);
            System.out.println("予期せぬ成功: 氏名未設定でもエラーが発生しませんでした");
            LogHandler.getInstance().log(Level.WARNING, "氏名未設定のテストが予期せず成功しました");
        } catch (IllegalStateException e) {
            System.out.println("テスト成功: 期待通りのエラーが発生しました - " + e.getMessage());
            LogHandler.getInstance().log(Level.INFO, "氏名未設定のテストが正常に完了しました: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("テスト失敗: 予期しないエラーが発生しました - " + e.getMessage());
            LogHandler.getInstance().logError("氏名未設定のテストで予期しないエラーが発生しました", e);
        }
    }

    /**
     * フリガナ未設定のテスト
     */
    private static void testMissingNameKana() {
        try {
            LogHandler.getInstance().log(Level.INFO, "フリガナ未設定のテストを開始します");

            EngineerDTO engineer = new EngineerBuilder()
                    .setId("ID00004")
                    .setName("山田太郎")
                    .setBirthDate(LocalDate.of(1990, 1, 15))
                    .setJoinDate(LocalDate.of(2020, 4, 1))
                    .setCareer(5)
                    .setProgrammingLanguages(Arrays.asList("Java", "Python"))
                    .build();

            printEngineerInfo(engineer);
            System.out.println("予期せぬ成功: フリガナ未設定でもエラーが発生しませんでした");
            LogHandler.getInstance().log(Level.WARNING, "フリガナ未設定のテストが予期せず成功しました");
        } catch (IllegalStateException e) {
            System.out.println("テスト成功: 期待通りのエラーが発生しました - " + e.getMessage());
            LogHandler.getInstance().log(Level.INFO, "フリガナ未設定のテストが正常に完了しました: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("テスト失敗: 予期しないエラーが発生しました - " + e.getMessage());
            LogHandler.getInstance().logError("フリガナ未設定のテストで予期しないエラーが発生しました", e);
        }
    }

    /**
     * 生年月日未設定のテスト
     */
    private static void testMissingBirthDate() {
        try {
            LogHandler.getInstance().log(Level.INFO, "生年月日未設定のテストを開始します");

            EngineerDTO engineer = new EngineerBuilder()
                    .setId("ID00005")
                    .setName("山田太郎")
                    .setNameKana("ヤマダタロウ")
                    .setJoinDate(LocalDate.of(2020, 4, 1))
                    .setCareer(5)
                    .setProgrammingLanguages(Arrays.asList("Java", "Python"))
                    .build();

            printEngineerInfo(engineer);
            System.out.println("予期せぬ成功: 生年月日未設定でもエラーが発生しませんでした");
            LogHandler.getInstance().log(Level.WARNING, "生年月日未設定のテストが予期せず成功しました");
        } catch (IllegalStateException e) {
            System.out.println("テスト成功: 期待通りのエラーが発生しました - " + e.getMessage());
            LogHandler.getInstance().log(Level.INFO, "生年月日未設定のテストが正常に完了しました: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("テスト失敗: 予期しないエラーが発生しました - " + e.getMessage());
            LogHandler.getInstance().logError("生年月日未設定のテストで予期しないエラーが発生しました", e);
        }
    }

    /**
     * 入社年月未設定のテスト
     */
    private static void testMissingJoinDate() {
        try {
            LogHandler.getInstance().log(Level.INFO, "入社年月未設定のテストを開始します");

            EngineerDTO engineer = new EngineerBuilder()
                    .setId("ID00006")
                    .setName("山田太郎")
                    .setNameKana("ヤマダタロウ")
                    .setBirthDate(LocalDate.of(1990, 1, 15))
                    .setCareer(5)
                    .setProgrammingLanguages(Arrays.asList("Java", "Python"))
                    .build();

            printEngineerInfo(engineer);
            System.out.println("予期せぬ成功: 入社年月未設定でもエラーが発生しませんでした");
            LogHandler.getInstance().log(Level.WARNING, "入社年月未設定のテストが予期せず成功しました");
        } catch (IllegalStateException e) {
            System.out.println("テスト成功: 期待通りのエラーが発生しました - " + e.getMessage());
            LogHandler.getInstance().log(Level.INFO, "入社年月未設定のテストが正常に完了しました: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("テスト失敗: 予期しないエラーが発生しました - " + e.getMessage());
            LogHandler.getInstance().logError("入社年月未設定のテストで予期しないエラーが発生しました", e);
        }
    }

    /**
     * エンジニア歴が負の値のテスト
     */
    private static void testNegativeCareer() {
        try {
            LogHandler.getInstance().log(Level.INFO, "エンジニア歴が負の値のテストを開始します");

            EngineerDTO engineer = new EngineerBuilder()
                    .setId("ID00007")
                    .setName("山田太郎")
                    .setNameKana("ヤマダタロウ")
                    .setBirthDate(LocalDate.of(1990, 1, 15))
                    .setJoinDate(LocalDate.of(2020, 4, 1))
                    .setCareer(-1)
                    .setProgrammingLanguages(Arrays.asList("Java", "Python"))
                    .build();

            printEngineerInfo(engineer);
            System.out.println("予期せぬ成功: エンジニア歴が負の値でもエラーが発生しませんでした");
            LogHandler.getInstance().log(Level.WARNING, "エンジニア歴が負の値のテストが予期せず成功しました");
        } catch (IllegalStateException e) {
            System.out.println("テスト成功: 期待通りのエラーが発生しました - " + e.getMessage());
            LogHandler.getInstance().log(Level.INFO, "エンジニア歴が負の値のテストが正常に完了しました: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("テスト失敗: 予期しないエラーが発生しました - " + e.getMessage());
            LogHandler.getInstance().logError("エンジニア歴が負の値のテストで予期しないエラーが発生しました", e);
        }
    }

    /**
     * プログラミング言語未設定のテスト
     */
    private static void testMissingProgrammingLanguages() {
        try {
            LogHandler.getInstance().log(Level.INFO, "プログラミング言語未設定のテストを開始します");

            EngineerDTO engineer = new EngineerBuilder()
                    .setId("ID00008")
                    .setName("山田太郎")
                    .setNameKana("ヤマダタロウ")
                    .setBirthDate(LocalDate.of(1990, 1, 15))
                    .setJoinDate(LocalDate.of(2020, 4, 1))
                    .setCareer(5)
                    .build();

            printEngineerInfo(engineer);
            System.out.println("予期せぬ成功: プログラミング言語未設定でもエラーが発生しませんでした");
            LogHandler.getInstance().log(Level.WARNING, "プログラミング言語未設定のテストが予期せず成功しました");
        } catch (IllegalStateException e) {
            System.out.println("テスト成功: 期待通りのエラーが発生しました - " + e.getMessage());
            LogHandler.getInstance().log(Level.INFO, "プログラミング言語未設定のテストが正常に完了しました: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("テスト失敗: 予期しないエラーが発生しました - " + e.getMessage());
            LogHandler.getInstance().logError("プログラミング言語未設定のテストで予期しないエラーが発生しました", e);
        }
    }

    /**
     * 技術力の評価が範囲外（1.0未満）のテスト
     */
    private static void testTechnicalSkillBelowRange() {
        try {
            LogHandler.getInstance().log(Level.INFO, "技術力の評価が範囲外（1.0未満）のテストを開始します");

            EngineerDTO engineer = new EngineerBuilder()
                    .setId("ID00009")
                    .setName("山田太郎")
                    .setNameKana("ヤマダタロウ")
                    .setBirthDate(LocalDate.of(1990, 1, 15))
                    .setJoinDate(LocalDate.of(2020, 4, 1))
                    .setCareer(5)
                    .setProgrammingLanguages(Arrays.asList("Java", "Python"))
                    .setTechnicalSkill(0.5)
                    .build();

            printEngineerInfo(engineer);
            System.out.println("予期せぬ成功: 技術力の評価が範囲外（1.0未満）でもエラーが発生しませんでした");
            LogHandler.getInstance().log(Level.WARNING, "技術力の評価が範囲外（1.0未満）のテストが予期せず成功しました");
        } catch (IllegalArgumentException e) {
            System.out.println("テスト成功: 期待通りのエラーが発生しました - " + e.getMessage());
            LogHandler.getInstance().log(Level.INFO, "技術力の評価が範囲外（1.0未満）のテストが正常に完了しました: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("テスト失敗: 予期しないエラーが発生しました - " + e.getMessage());
            LogHandler.getInstance().logError("技術力の評価が範囲外（1.0未満）のテストで予期しないエラーが発生しました", e);
        }
    }

    /**
     * 技術力の評価が範囲外（5.0超過）のテスト
     */
    private static void testTechnicalSkillAboveRange() {
        try {
            LogHandler.getInstance().log(Level.INFO, "技術力の評価が範囲外（5.0超過）のテストを開始します");

            EngineerDTO engineer = new EngineerBuilder()
                    .setId("ID00010")
                    .setName("山田太郎")
                    .setNameKana("ヤマダタロウ")
                    .setBirthDate(LocalDate.of(1990, 1, 15))
                    .setJoinDate(LocalDate.of(2020, 4, 1))
                    .setCareer(5)
                    .setProgrammingLanguages(Arrays.asList("Java", "Python"))
                    .setTechnicalSkill(5.5)
                    .build();

            printEngineerInfo(engineer);
            System.out.println("予期せぬ成功: 技術力の評価が範囲外（5.0超過）でもエラーが発生しませんでした");
            LogHandler.getInstance().log(Level.WARNING, "技術力の評価が範囲外（5.0超過）のテストが予期せず成功しました");
        } catch (IllegalArgumentException e) {
            System.out.println("テスト成功: 期待通りのエラーが発生しました - " + e.getMessage());
            LogHandler.getInstance().log(Level.INFO, "技術力の評価が範囲外（5.0超過）のテストが正常に完了しました: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("テスト失敗: 予期しないエラーが発生しました - " + e.getMessage());
            LogHandler.getInstance().logError("技術力の評価が範囲外（5.0超過）のテストで予期しないエラーが発生しました", e);
        }
    }

    /**
     * 受講態度の評価が範囲外のテスト
     */
    private static void testLearningAttitudeOutOfRange() {
        try {
            LogHandler.getInstance().log(Level.INFO, "受講態度の評価が範囲外のテストを開始します");

            EngineerDTO engineer = new EngineerBuilder()
                    .setId("ID00011")
                    .setName("山田太郎")
                    .setNameKana("ヤマダタロウ")
                    .setBirthDate(LocalDate.of(1990, 1, 15))
                    .setJoinDate(LocalDate.of(2020, 4, 1))
                    .setCareer(5)
                    .setProgrammingLanguages(Arrays.asList("Java", "Python"))
                    .setLearningAttitude(5.5)
                    .build();

            printEngineerInfo(engineer);
            System.out.println("予期せぬ成功: 受講態度の評価が範囲外でもエラーが発生しませんでした");
            LogHandler.getInstance().log(Level.WARNING, "受講態度の評価が範囲外のテストが予期せず成功しました");
        } catch (IllegalArgumentException e) {
            System.out.println("テスト成功: 期待通りのエラーが発生しました - " + e.getMessage());
            LogHandler.getInstance().log(Level.INFO, "受講態度の評価が範囲外のテストが正常に完了しました: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("テスト失敗: 予期しないエラーが発生しました - " + e.getMessage());
            LogHandler.getInstance().logError("受講態度の評価が範囲外のテストで予期しないエラーが発生しました", e);
        }
    }

    /**
     * コマンドライン引数を使用したカスタムテスト
     */
    private static void testCustom(String[] args) {
        try {
            LogHandler.getInstance().log(Level.INFO, "カスタムテストを開始します");

            // 必須パラメータの解析
            String id = args[1];
            String name = args[2];
            String nameKana = args[3];
            LocalDate birthDate = LocalDate.parse(args[4], DATE_FORMATTER);
            LocalDate joinDate = LocalDate.parse(args[5], DATE_FORMATTER);
            int career = Integer.parseInt(args[6]);

            // プログラミング言語リストの解析
            List<String> programmingLanguages = new ArrayList<>();
            if (args[7].contains(",")) {
                programmingLanguages = Arrays.asList(args[7].split(","));
            } else {
                programmingLanguages.add(args[7]);
            }

            // EngineerBuilderの作成と必須項目の設定
            EngineerBuilder builder = new EngineerBuilder()
                    .setId(id)
                    .setName(name)
                    .setNameKana(nameKana)
                    .setBirthDate(birthDate)
                    .setJoinDate(joinDate)
                    .setCareer(career)
                    .setProgrammingLanguages(programmingLanguages);

            // 任意パラメータの設定（存在する場合）
            if (args.length > 8) {
                // キャリア履歴
                builder.setCareerHistory(args[8]);
            }

            if (args.length > 9) {
                // 研修履歴
                builder.setTrainingHistory(args[9]);
            }

            if (args.length > 10) {
                // 技術力
                builder.setTechnicalSkill(Double.parseDouble(args[10]));
            }

            if (args.length > 11) {
                // 受講態度
                builder.setLearningAttitude(Double.parseDouble(args[11]));
            }

            if (args.length > 12) {
                // コミュニケーション能力
                builder.setCommunicationSkill(Double.parseDouble(args[12]));
            }

            if (args.length > 13) {
                // リーダーシップ
                builder.setLeadership(Double.parseDouble(args[13]));
            }

            if (args.length > 14) {
                // 備考
                builder.setNote(args[14]);
            }

            // EngineerDTOの構築
            EngineerDTO engineer = builder.build();

            printEngineerInfo(engineer);
            System.out.println("カスタムテストが正常に完了しました");
            LogHandler.getInstance().log(Level.INFO, "カスタムテストが正常に完了しました");
        } catch (DateTimeParseException e) {
            System.out.println("日付の解析に失敗しました: " + e.getMessage());
            System.out.println("日付形式は yyyy-MM-dd である必要があります（例: 1990-01-15）");
            LogHandler.getInstance().logError("カスタムテストで日付の解析に失敗しました", e);
        } catch (NumberFormatException e) {
            System.out.println("数値の解析に失敗しました: " + e.getMessage());
            LogHandler.getInstance().logError("カスタムテストで数値の解析に失敗しました", e);
        } catch (Exception e) {
            System.out.println("テスト失敗: " + e.getMessage());
            LogHandler.getInstance().logError("カスタムテストが失敗しました", e);
        }
    }

    /**
     * EngineerDTOの情報を表示
     */
    private static void printEngineerInfo(EngineerDTO engineer) {
        System.out.println("\n===== エンジニア情報 =====");
        System.out.println("社員ID: " + engineer.getId());
        System.out.println("氏名: " + engineer.getName());
        System.out.println("フリガナ: " + engineer.getNameKana());
        System.out.println("生年月日: " + formatDate(engineer.getBirthDate()));
        System.out.println("入社年月: " + formatDate(engineer.getJoinDate()));
        System.out.println("エンジニア歴: " + engineer.getCareer() + "年");
        System.out.println("プログラミング言語: " + String.join(", ", engineer.getProgrammingLanguages()));

        // 任意項目（nullの場合は表示しない）
        if (engineer.getCareerHistory() != null) {
            System.out.println("経歴: " + engineer.getCareerHistory());
        }
        if (engineer.getTrainingHistory() != null) {
            System.out.println("研修の受講歴: " + engineer.getTrainingHistory());
        }
        if (engineer.getTechnicalSkill() > 0) {
            System.out.println("技術力: " + engineer.getTechnicalSkill());
        }
        if (engineer.getLearningAttitude() > 0) {
            System.out.println("受講態度: " + engineer.getLearningAttitude());
        }
        if (engineer.getCommunicationSkill() > 0) {
            System.out.println("コミュニケーション能力: " + engineer.getCommunicationSkill());
        }
        if (engineer.getLeadership() > 0) {
            System.out.println("リーダーシップ: " + engineer.getLeadership());
        }
        if (engineer.getNote() != null && !engineer.getNote().isEmpty()) {
            System.out.println("備考: " + engineer.getNote());
        }
        System.out.println("登録日時: " + formatDate(engineer.getRegisteredDate()));
        System.out.println("==========================\n");
    }

    /**
     * LocalDateを文字列にフォーマット
     */
    private static String formatDate(LocalDate date) {
        if (date == null) {
            return "未設定";
        }
        return date.format(DATE_FORMATTER);
    }
}