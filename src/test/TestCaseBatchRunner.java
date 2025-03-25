package test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import util.LogHandler;
import util.MessageEnum;
import java.util.logging.Level;

/**
 * EngineerBuilder.javaの機能試験を一括して実行するバッチ処理クラス
 * TestCoreSystemの複数のテストケースを連続して実行します
 *
 * 実行方法：
 * java test.TestCaseBatchRunner
 *
 * @author Test Engineer
 * @version 1.0
 */
public class TestCaseBatchRunner {

    // 実行するテストケース番号のリスト
    private static final List<String> TEST_CASES = Arrays.asList(
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");

    // カスタムテストの設定
    private static final String[][] CUSTOM_TESTS = {
            {
                    "13", "ID00001", "山田太郎", "ヤマダタロウ", "1990-01-15", "2015-04-01", "8",
                    "Java,Python,JavaScript", "大手SIer5年,ベンチャー3年", "Java研修,AWS研修",
                    "4.5", "4.0", "3.5", "3.0", "特になし"
            },
            {
                    "13", "ID00002", "鈴木花子", "スズキハナコ", "1997-06-21", "2024-04-01", "0",
                    "Java,SQL", "新卒入社", "新人研修受講中",
                    "2.0", "4.5", "3.8", "2.5", "コミュニケーション能力が高い"
            },
            {
                    "13", "ID00003", "佐藤次郎", "サトウジロウ", "1980-11-05", "2010-04-01", "15",
                    "Java,C++,Python,Go,SQL,Kotlin", "メガバンクシステム開発10年,当社5年",
                    "プロジェクトマネジメント研修,AWS認定ソリューションアーキテクト",
                    "4.8", "4.2", "4.7", "4.9", "部門リーダー、メンター担当"
            }
    };

    public static void main(String[] args) {
        // ログハンドラの初期化
        initializeLogHandler();

        try {
            System.out.println("=====================================================");
            System.out.println("エンジニア情報管理システム - テストケース一括実行開始");
            System.out.println("=====================================================");
            System.out.println();

            // 標準テストケースを実行
            runStandardTests();

            // カスタムテストを実行
            runCustomTests();

            System.out.println("=====================================================");
            System.out.println("すべてのテストケースの実行が完了しました");
            System.out.println("テストログは test_logs ディレクトリに保存されています");
            System.out.println("=====================================================");
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
            LogHandler.getInstance().log(MessageEnum.LOG_INFO_SYSTEM_START);
            LogHandler.getInstance().log(Level.INFO, "テストケース一括実行を開始します");
        } catch (IOException e) {
            System.err.println("ログハンドラの初期化に失敗しました: " + e.getMessage());
        }
    }

    /**
     * 標準テストケースを実行します
     */
    private static void runStandardTests() {
        for (String testCase : TEST_CASES) {
            System.out.println("-----------------------------------------------------");
            System.out.println("テストケース " + testCase + " を実行します...");
            System.out.println("-----------------------------------------------------");

            try {
                // TestCoreSystemの実行
                TestCoreSystem.main(new String[] { testCase });
                System.out.println();
            } catch (Exception e) {
                System.out.println("テストケース " + testCase + " の実行中にエラーが発生しました: " + e.getMessage());
                LogHandler.getInstance().logError("テストケース " + testCase + " の実行に失敗しました", e);
            }

            // テスト間の区切り
            System.out.println();
        }
    }

    /**
     * カスタムテストを実行します
     */
    private static void runCustomTests() {
        for (int i = 0; i < CUSTOM_TESTS.length; i++) {
            String[] testArgs = CUSTOM_TESTS[i];
            String testName = testArgs[2] + "（ID: " + testArgs[1] + "）";

            System.out.println("-----------------------------------------------------");
            System.out.println("カスタムテスト " + (i + 1) + ": " + testName + " を実行します...");
            System.out.println("-----------------------------------------------------");

            try {
                // TestCoreSystemの実行
                TestCoreSystem.main(testArgs);
                System.out.println();
            } catch (Exception e) {
                System.out.println("カスタムテスト " + testName + " の実行中にエラーが発生しました: " + e.getMessage());
                LogHandler.getInstance().logError("カスタムテスト " + testName + " の実行に失敗しました", e);
            }

            // テスト間の区切り
            System.out.println();
        }
    }
}